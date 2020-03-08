# Front end framework considerations
#### Option 1 - Using flask templates.
Flask provides a templating engine with conditional statements and for loops. A framework like react could be used, but that would hinder code architectire. 
#### Option 2 - React.js
Using React, which provides client routing is served in  node.js during develpoment, provides better tools for development of code architecture. Client side development involves JavaScript modules.

# Functionalities of the dashboard
- Modifing model paramaters
  - ability to upload a TF model
  - specifiy type of model (linear regression... what else?) and types of features (linear features, qudratic etc...), number of features (excluding some )
  - choose ration between training/validation/test data
  - show progress bar, show trainining/accuracy graphs
  - 