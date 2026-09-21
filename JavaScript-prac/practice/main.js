'use strict';

// 引入hello模块:
var greet = require('./hello');

var s = 'zhaoq';

greet.hello();
greet.greet(s); // Hello, Michael!