var express = require('express');
var PORT = process.env.NODE_PORT || 8080;
process.title = process.env.PROCESS_TITLE || "backend-mustela";
var app;
var bodyParser = require('body-parser');
app = express();
app.set('trust proxy', true);
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json({type: '*/*'}));
var db = require('./db');
require('./index')(app);

// Listen on port specified.
app.listen(PORT);

// Output server start success to terminal.
console.log("[SERVER] Running on port " + PORT + "");
