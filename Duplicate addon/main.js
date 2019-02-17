let body = document.getElementById('issue_body'),
	ctx = document.getElementsByClassName('form-actions')[0],
	footer = document.getElementsByClassName('timeline-comment-wrapper timeline-new-comment composer')[0],
	resAdded = false

const btnInject = '<p id="duplicate-button" \
					class="btn btn-primary" \
					style="float:right"> \
					Check for duplicates</p>'

function resultInject(res){
	resAdded = true
	return '<p id="duplicate-results" \
			 style="width: 100%; \
			height: 200px; background: \
			lightgrey; border-radius: 5px; \
			padding: 10px;">'
			+ res.origin +
			'</p>'
}

console.log('extension fired')
ctx.innerHTML += btnInject

window.addEventListener('click', function(e){
	if(e.target.id == 'duplicate-button'){
		window.open('http://localhost:8081/results?url=' + window.location + '&text=' + body.value)
	}
})