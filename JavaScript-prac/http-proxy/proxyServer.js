
 // lsof -i -P -n |grep LISTEN     查看8080是否正常在监听中
 // 随便找几个  12123.net  12126.net 这种http网页测试一下,可用
 // https 测试了一下百度，也可用

 
const fs = require('fs');

var hoxy = require('hoxy');

KEY_PRIVATE_PATH_YOUR='/Users/zhaoq0103/.ssh/my-private-root-ca.key.pem'
CERT_PUBLIC_PATH_YOUR='/Users/zhaoq0103/.ssh/my-private-root-ca.crt.pem'


var proxy = hoxy.createServer({
    certAuthority: {
          key: fs.readFileSync(KEY_PRIVATE_PATH_YOUR),
          cert: fs.readFileSync(CERT_PUBLIC_PATH_YOUR),
        }
}).listen(8080);


proxy.intercept({

  // intercept during the response phase
  phase: 'response',

  // only intercept html pages
  mimeType: 'text/html',

  // expose the response body as a cheerio object
  // (cheerio is a jQuery clone)
  as: '$'
}, function(req, resp) {

  resp.$('title').text('赵强');
  // all page titles will now say "Unicorns!"
});

console.log('Hoxy proxy server running on port 8080');

