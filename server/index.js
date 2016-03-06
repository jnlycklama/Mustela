var express = require('express');
var db = require('./db');
var request = require('request');
var request2 = require('request');


module.exports = function(app) {
	'use strict'
		var router = express.Router();
	router.route('/').get(function(req, res, next) {
		res.end("<a href='http://www.runescape.com'>node.js tutorials</a>");
	});

	router.route('/create/:username/blob/:blobname').post(function (req, res, next) {
		var username = req.params.username;
		var blobname = req.params.blobname;
		if (!username || !blobname) {
			res.status(400).json({"err":"Bad request"});
			return;
		}
		db.query("INSERT INTO faces (username, blob) VALUES ($1, $2)", [username, blobname], function (err, result) {
			if (err) {
				console.log("Error on creating account in the database");
				res.status(400).json({"err":"Account creation failed."});
			}
			res.json({"success":"Account Created"});
		});
	});

	router.route('/authenticate/:username/blob/:blobname').post(function(req, res, next) {
		var username = req.params.username;
		var blobname = req.params.blobname;
		db.query('SELECT blob FROM faces WHERE username = $1', [username], function(err, result) {
			var blob = result.rows[0].blob;
			var newBlobURL =    "https://mustelastorage.blob.core.windows.net/images/" + blobname;
			var storedBlobURL = "https://mustelastorage.blob.core.windows.net/images/" + blob;
			var reqURL = 'https://api.projectoxford.ai/face/v1.0/detect';
			var request1 = {
				method: "POST",
			url: reqURL,
			headers: {
				"Ocp-Apim-Subscription-Key": "87a8c6b31cab47ae8fa5de8c4837e71d",
			'Content-Type': 'application/json'
			},
			json: {
				"url": newBlobURL
			}
			};
			request(request1, function(err, response, body) {
				if (err) {
					console.log(JSON.stringify(err));
					res.json(err);
					return;
				}
				if (response.statusCode != 200) {
					console.log(JSON.stringify(response));
				}
				console.log(body);
				var fid = body[0].faceId;
				var request2 = {
					method: "POST",
				url: reqURL,
				headers: {
					"Ocp-Apim-Subscription-Key": "87a8c6b31cab47ae8fa5de8c4837e71d",
				'Content-Type': 'application/json'
				},
				json: {
					"url": storedBlobURL
				}
				};
				request(request2, function(err, response, body) {
					if (err) {
						console.log(JSON.stringify(err));
						res.json(err);
						return;
					}
					if (response.statusCode != 200) {
						console.log(JSON.stringify(response));
					}
					console.log(body);
					var faceid = body[0].faceId;
					var request3 = {
						method: "POST",
					url: 'https://api.projectoxford.ai/face/v1.0/verify',
					headers: {
						'Ocp-Apim-Subscription-Key': '87a8c6b31cab47ae8fa5de8c4837e71d',
					'Content-Type': 'application/json'
					},
					json: {
						"faceId1": fid,
					"faceId2": faceid
					}
					};
					console.log("Faces");
					console.log(fid);
					console.log(faceid);
					request(request3, function(err, response, body) {
						if (err) {
							console.log(JSON.stringify([err, response, body]));
							res.json([err, response, body]);
							return;
						}
						res.json(body);
					});
				});
			});
		});
	});
	app.use(router);
}
