'use strict'
//Database Logic Stuff
var pg = require('pg');
var logger = require('winston');
pg.defaults.user = "alex";
pg.defaults.password = "Ferret123";
pg.defaults.database = "alex";
pg.defaults.host = "/var/run/postgresql";

function query(text, values, callback) {
  pg.connect(function(err, client, done) {
    if (err) {
      logger.error("[SERVER] Error getting connection from pool.", err);
    }
    client.query(text, values, function(err, result) {
      done();
      callback(err, result);
    })
  });
}

module.exports.query=query;
