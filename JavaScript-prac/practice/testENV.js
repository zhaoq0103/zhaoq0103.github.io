'use strict'

var fs = require('fs');



if (typeof(window) === 'undefined') {
    console.log('node.js');
} else {
    console.log('browser');
}


// var ws1 = fs.createWriteStream('output1.txt', 'utf-8');
// ws1.write('使用Stream写入文本数据...\n');
// ws1.write('END.');
// ws1.end();


// 打开一个流:
var rs = fs.createReadStream('./practice/sortArray.js', 'utf-8');
rs.on('data', function (chunk) {
    console.log('DATA:>>>')
    console.log(chunk);
});

rs.on('end', function () {
    console.log('>>>END');
});

rs.on('error', function (err) {
    console.log('ERROR: ' + err);
});
