module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [2, 'always', [
      'FEATURE', 
      'FIX',      
      'DOCS',     
      'REFACTOR', 
      'TEST',     
      'CI',       
      'HOTFIX',   
      'CHORE',    
      'RELEASE',  
      'SP',       
      'BUGREPORT',
      'MERGE',    
      'STYLES',
      'CONFIGURATION',
    ]],
    'type-case': [2, 'always', 'upper-case'],
    'subject-case': [2, 'always', 'sentence-case'], 
    'type-empty': [2, 'never'], 
    'subject-empty': [2, 'never'], 
  },
  parserPreset: {
    parserOpts: {
      headerPattern: /^(\(([\w ]+)\))?: (.+)$/, 
      headerCorrespondence: ['type', 'type', 'subject'], 
    },
  },
};
