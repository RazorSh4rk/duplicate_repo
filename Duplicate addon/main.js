/*let body = document.getElementById('issue_body'),
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
		req()
	}
})

function req(){
	let b = body.value
	if(b.length != 0){
		fetch('http://localhost:8081/detect', {
		        method: "POST",
		        body: JSON.stringify({
		        	"URL" : window.location,
		        	"TEXT" : b
		        })
		    })
			.then((resp) => resp.json())
			.then((res) => {
				console.log(res)
				if(!resAdded)
					footer.innerHTML += resultInject(res)
				else
					document.getElementById('duplicate-results')
						.innerHTML = res.origin
			})
	}else{
		if(!resAdded)
			footer.innerHTML += 
				resultInject('gimme something to work with')
		else
			document.getElementById('duplicate-results')
				.innerHTML = 'gimme something to work with'
	}
}
*/
window.open('http://localhost:8081/ping')
//let a = browser.runtime.getURL('/test.html'))