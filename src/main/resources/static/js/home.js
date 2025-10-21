document.addEventListener('DOMContentLoaded', function () {
  fetch('/home/random-quiz')
    .then(response => {
 if(!response.ok){
        renderNoQuizMessage();
 return null;
 }
 return response.json();
 })
    .then(data => {
        renderQuiz(data);
    });

  function renderQuiz(data) {
    const quizHtml = `
      <div class="todayquiz-card">
        <div class="todayquiz-header">
          <span class="category">📚 카테고리: ${data.category_name} </span>
          <span class="points">✨ 포인트: ${data.points}점 (정답 시 2배 적립)</span>
        </div>
        <div class="todayquiz-content">
          <h2 class="todayquiz-question">${data.content}</h2>
          <input type="hidden" id="questionNo" value="${data.question_no}" />
          <input type="text" id="answerInput" placeholder="정답을 입력하세요" class="todayquiz-input" />
          <button id="submitBtn" class="todayquiz-submit-btn">🎯 제출</button>
        </div>
        <div id="resultMessage" class="todayquiz-result"></div>
      </div>
    `;
    document.getElementById('todayquiz-box').innerHTML = quizHtml;

    document.getElementById('submitBtn').addEventListener('click', function () {
      const answer = document.getElementById('answerInput').value.trim();
      const questionNo = document.getElementById('questionNo').value;
      const points = data.points;

      if (answer === "") {
        alert("정답을 입력해주세요! 🍀");
        return;
      }

      fetch('/home/submit-answer', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ questionNo, answer, points })
      })
        .then(res => res.json())
        .then(result => {
          const msg = document.getElementById('resultMessage');
          msg.textContent = result.message;
          msg.className = result.isCorrect ? 'todayquiz-result correct' : 'todayquiz-result wrong';
          document.getElementById('answerInput').disabled = true;
          document.getElementById('submitBtn').disabled = true;
        });
    });
  }

  function renderNoQuizMessage() {
   const noQuizHtml = `
     <div class="todayquiz-card no-quiz-message">
       <div class="todayquiz-content">
         <h4 class="todayquiz-question">
           오늘의 퀴즈가 없습니다.<br>✨ 위키에서 더 많은 문제를 풀어보세요. ✨
         </h4>
       </div>
     </div>
   `;
   document.getElementById('todayquiz-box').innerHTML = noQuizHtml;
 }
});