var appServices = angular.module('myApp.services');

appServices.service('QuestionService', ["$resource", '$http', function($resource, $http){

    var askQuestion = function(question, conversationId, success, failure){
        $http({
            method: "GET",
            url: "/question",
            params: {
                query: question,
                conversationId: conversationId
            }
        }).then(success, failure);
    };


    return {
        ask: askQuestion
    }

}]);
