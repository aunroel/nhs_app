## [Wojtek] project consderatinos summary (week 7)

### 1. Confirm to our roadmap and goals
Confirm to him learning will be done on the server, and prediction will be done on mobile (prediction possible with TF Lite, learning not). Confirm it is currently not possible on mobile to do FML (within our timeframe), no official framework has been released yet, but such are on thir way (OpenMined).
 
Then why are we doing prediction on mobile at all? 
Because we are exploring all possible ways of introducing ML/FML to mobile devices and treat one outcome of this project as a collection of take-aways we learned, e.g. what is challenging about integrating a library (done by us) with 3rd party apps, what practical considerations there are (e.g. how we should schedule server calls on devices) etc. If we did everything on the server, that would be a classical approach - nothing novel in the project, just REST API calls from the mobile apps.



### 2. Linode node access, using a seperate DB and why 
Remind him to finally give us access to open a new linode node. Clarify why we don't want to use the undergrads database as our database: we consider this to be a seperation of concerns. We do not want to be concerned on how they organise the data, what purpose they use it for, what other data they use that we don't need for training. In real life, when you intergate with a 3rd party app, you treate them as data provider, you don't have access to their database directly (or access to modify it!). UNLESS all the 3rd party apps are on NHS servers, then it doesn't make sense to duplicate data for this purpose, then probably you determine some common DB structure among two projects (the first: doctors who help the patients and anaylise their demographics, the second: scientists who do ML). But even practically in our course, we don't what to depend on undegrads - e.g. if they decide to test 'clean database function' in the middle of our training, it's not fun.



### 3. We are making a web dashboard and why
One aim of the web dashboard is to give on overview and ability to modify learning model currently in use (type of linear regression, its params). The other aim here is to explore ideas of what kind of tools can be useful for data scientists, and a platform for them to collaborate on their research. Such dashboards most likely exist in industry, however we are exploring use cases specific to our project, propose our own approach, and present practical takeaways (what features we ended up using in the dashboard, what not and why).   

Dashboard features we are providing (we consider to be most useful and possible within the timeframe):
- ability to upload TF models (regression types), coded locally on a user's machine (TF model 'save()'), with predefined input / output structure
- ability to train uploaded models
- ability to compare accuracy/loss/training graphs of all the uploaded models
- login system with each user being able to add their comments to each model (like on trello board)
- review of the database structure, number of data, number of mobile device users
"What do you think Joseph?"