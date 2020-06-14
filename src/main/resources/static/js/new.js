document.getElementById('baton').addEventListener("click", function() {
    document.querySelector('.bg-modal-baton').style.display = 'flex';
});
document.querySelector('.close-baton').addEventListener('click',function() {
    document.querySelector('.bg-modal-baton').style.display = 'none';

});