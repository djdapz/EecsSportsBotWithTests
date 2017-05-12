
angular.module("myApp.AppController", []);
angular.module("myApp.services", []);


angular.module("myApp", ["ngResource",
                        "myApp.AppController",
                        "myApp.services"]);
