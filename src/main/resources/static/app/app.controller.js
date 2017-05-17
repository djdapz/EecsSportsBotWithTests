
var AppController = function($scope, $timeout, QuestionService) {
    $scope.conversationId = undefined;

    $scope.waiting = false;
    $scope.waitingText = '...';

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
                    $scope.waiting = false;
                    $scope.chat.unshift({
                        role: "CHATBOT",
                        text: response.data.response
                    });

                    $scope.conversationId = response.data.conversationId;
                },

                //failure
                function(error){
                    $scope.waiting = false;
                    $scope.chat.unshift({
                        role: "CHATBOT",
                        text: "ERROR: reaching server"
                    });
                    console.log(error);
                }
            );

            $scope.queryText = undefined;
            $scope.waiting = true;

        }
    };

    function animateWaitingText(){
        if($scope.waitingText.length >= 3){
            $scope.waitingText = "."
        }else{
            $scope.waitingText+= '.';
        }
        $timeout(animateWaitingText, 300);
    }

    animateWaitingText();


};

angular
    .module('myApp')
    .controller("AppController", AppController );

AppController.$inject = ['$scope', "$timeout", "QuestionService"];

