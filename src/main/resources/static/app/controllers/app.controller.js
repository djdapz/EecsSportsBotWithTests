
var AppController = function($scope, QuestionService) {
    $scope.conversationId = undefined;

    $scope.chat = [
        {
            role: "CHATBOT",
            text: "What's up?"
        }
    ];

    $scope.queryText = undefined;

    $scope.sendClicked = function(){
        if($scope.queryText.length > 0){
            $scope.chat.unshift({
                role:"USER",
                text: $scope.queryText
            });

            QuestionService.ask($scope.queryText, $scope.conversationId,

                //success
                function(response){
                    $scope.chat.unshift({
                        role: "CHATBOT",
                        text: response.data.response
                    });

                    $scope.conversationId = response.data.conversationId;
                },

                //failure
                function(error){
                    $scope.chat.unshift({
                        role: "CHATBOT",
                        text: "ERROR: reaching server"
                    });
                    console.log(error);
                }
            );

            $scope.queryText = undefined;

        }
    };


};

angular
    .module('myApp')
    .controller("AppController", AppController );

AppController.$inject = ['$scope', "QuestionService"];

