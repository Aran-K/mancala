var gameId;

$(document).ready(function() {


function makeMove(x) {
    $.ajax({
      type: "POST",
      url: "/makeMove",
      data: { gameId: gameId, pit: x },
      success: function(data) {
        reDrawBoard(data)
      }
    });
}

function reDrawBoard(game) {
   // Set pit numbers
    for (i = 0; i < 6; i++) {
        $('.player-one-pit.'+i).html(game.playerOne.pits[i]);
        $('.player-two-pit.'+i).html(game.playerTwo.pits[i]);
    }

    // Set store counts
    $('.player-one.store').html(game.playerOne.homePitCount);
    $('.player-two.store').html(game.playerTwo.homePitCount);

    // Update 'active' player
    if (game.playerOne.active) {
        $('.current-player').html(game.playerOne.name);
        $('.player-one-pit').addClass('active-pit');

        $('.player-two-pit').removeClass('active-pit');
        updateOnClickHandlers();
    } else if (game.playerTwo.active){
        $('.current-player').html(game.playerTwo.name);
        $('.player-two-pit').addClass('active-pit');

        $('.player-one-pit').removeClass('active-pit');
        updateOnClickHandlers();
    } else { // Game over
        // Unbind the click handlers
        $('.player-two-pit').unbind();
        $('.player-one-pit').unbind();

        // Determine winner, update style and alert
        var winner;
        if (game.playerOne.homePitCount > game.playerTwo.homePitCount) {
            winner = game.playerOne.name;
            $('.player-one-pit').addClass('active-pit');
            $('.player-two-pit').removeClass('active-pit');
        } else {
            winner = game.playerTwo.name;
            $('.player-two-pit').addClass('active-pit');
            $('.player-one-pit').removeClass('active-pit');
        }
        $('.status').html("Winner is " + winner + "!");
        alert("Winner is " + winner + "!");
    }
}
function updateOnClickHandlers() {
    // Update onclick
    $('.player-two-pit').unbind();
    $('.player-one-pit').unbind();
    $('.active-pit').on('click', function () {
        var classes = $(this).attr("class").split(" ");
        var pitNum = classes[classes.length-2];
        makeMove(pitNum);

    });

}

function newGame() {
    $.ajax({
      type: "GET",
      url: "/newGame",
      success: function(data) {
        gameId = data.id
        reDrawBoard(data)
      }
    });
}


$('.new-game').on('click', function() {
    newGame();
});

newGame();

}) // end of document ready

