// var key = "H1KP%2BzxNPWOCIedTdOuZhP5gy1gcAOtAX%2Fy%2B9eLJRFY%3D";//172.120.120.88

var key = "1dB%2FwPm4PhwY0aOXV%2B2TbNlEqHJ6TUbIjnVazTDO%2F5M%3D";//e.dituhui.com->www
// var key = "inbTxCaWslCxWX1dmAvGy9DdFkz%2BMnAK3UHI7TTJL7c%3D";//www->www

// var key = "U%2FYWdrs0XAMBb%2B7Mp7nJtPrpU6f%2FWJwiDhEZYQkBrhs%3D";

// var key = "BrpoVXWmXJsmamvHYPhzylST41F2iF8rTONIeDnDaTs%3D";

var host = "http://services.supermapcloud.com";
// var host = "http://192.168.10.251:8080";
// var host = "http://labs.supermapcloud.net";

//var key = "q5zq%2BpwGhL51qpdeI1Nz74vQFJKaVHDjKy4cmNcjDp4%3D"; //10.251-->www


// var script = '<script type="text/javascript" src="http://services.supermapcloud.com/iserver/api/iclient-7c/javascript/m.js"></script>';
// var script = '<script type="text/javascript" src="http://localhost:8010/iclient-6r/api.js"></script>';



var script = '<' + 'script type="text/javascript" src="'+host+'/iserver/api?key='+key+'&v=1.3"' + '><' + '/script>';
document.writeln(script);