// Triggers a function after a button click, here we are going to redirect the user to a selected game page
document.getElementById("Chess").onclick = () => {
    console.log("redirection");
    window.location.assign("chess.html");
};

/*
Commented because checkers is not imported yet
document.getElementById("Checkers").onclick = () => {
    window.location.assign("checkers/checkers.html");
};
 */