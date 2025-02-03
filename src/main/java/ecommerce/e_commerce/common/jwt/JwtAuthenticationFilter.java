package ecommerce.e_commerce.common.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;


    /**
    * Filters incoming requests to check for a valid JWT token.
    * If a valid token is found, sets the authenticated user in the SecurityContext.
    *
    * @param request     the HTTP request
    * @param response    the HTTP response
    * @param filterChain the filter chain
    * @throws ServletException if an error occurs during filtering
    * @throws IOException      if an input or output error occurs during filtering
    */
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
    )throws ServletException, IOException {

        //Get token 
        final String token = getTokenFromRequest(request);
        final String username;

        if (token==null){
            filterChain.doFilter(request, response);
            return;
        }

        //Get username of token
        username=jwtService.getUsernameFromToken(token);


        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                
            //Load data from database
            UserDetails userDetails=userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)){
                    
                //Create objet with data and permissions
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set the authenticated user in the SecurityContext to establish the security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
    

    /**
    * Extracts the JWT token from the Authorization header of the HTTP request.
    * 
    * @param request the HTTP request
    * @return the JWT token if present and valid, or null if not
    */
    private String getTokenFromRequest(HttpServletRequest request) {

        //Get header 
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);

        //Valid content to header
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);//Extract token
        }

        return null;
    }
}
