// 문제유형 선택 시 정답 필드 변경
$(document).on('change', 'select[name="typeCd"]', function() {
 const selectedType = $(this).val();
 const answerContainer = $('#answerContainer');

 if (selectedType === '1') {
 // 단답형 → textarea 사용
 answerContainer.html(`
              <label class="form-label">정답</label>
              <textarea class="form-control" name="answer" rows="4" required></textarea>
              `);

 } else if (selectedType === '2') {
 // OX형 → select box로 변경
 answerContainer.html(`
              <label class="form-label">정답</label>
              <select class="form-select" name="answer" required>
                <option value="">-- 선택하세요 --</option>
                <option value="O">O</option>
                <option value="X">X</option>
              </select>
            `);
 } else if (selectedType === '3') {
 answerContainer.html(`
              <label class="form-label">정답 선택지</label>
               <div id="choices-wrapper">
                 <div class="input-group mb-2">
                   <span class="input-group-text">1</span>
                   <input type="hidden" class="form-control" name="choices[0].choiceNo" value="1">
                   <input type="text" class="form-control" name="choices[0].content" placeholder="보기 1 입력" required>
                   <div class="input-group-text">
                     <input type="radio" name="answer" value="1" required> 정답
                   </div>
                 </div>
                 <div class="input-group mb-2">
                   <span class="input-group-text">2</span>
                   <input type="hidden" class="form-control" name="choices[1].choiceNo" value="2">
                   <input type="text" class="form-control" name="choices[1].content" placeholder="보기 2 입력" required>
                   <div class="input-group-text">
                     <input type="radio" name="answer" value="2"> 정답
                   </div>
                 </div>
               </div>
               <button type="button" class="main-btn primary-btn btn-hover" id="add-choice-btn">선택지 추가</button>
             `);
 }
});

let choiceIndex = 2; // 기본으로 1,2 두 개 이미 있음

$(document).on("click", "#add-choice-btn", function() {
 const wrapper = $("#choices-wrapper");
 const newChoice = `
            <div class="input-group mb-2">
              <span class="input-group-text">${choiceIndex + 1}</span>
              <input type="hidden" class="form-control" name="choices[${choiceIndex}].choiceNo" value="${choiceIndex + 1}">
              <input type="text" class="form-control" name="choices[${choiceIndex}].content" placeholder="보기 ${choiceIndex + 1} 입력" required>
              <div class="input-group-text">
                <input type="radio" name="answer" value="${choiceIndex + 1}"> 정답
              </div>
            </div>
          `;
 wrapper.append(newChoice);
 choiceIndex++;
});


function checkField() {
 const typeCd = $('select[name="typeCd"]').val();
 const content = $('textarea[name="content"]').val().trim();
 const points = $('select[name="points"]').val();
 const category = $('select[name="categoryName"]').val();
 const levelCd = $('select[name="levelCd"]').val();
 const answerField = $('[name="answer"]');
 const answer = answerField.val()?.trim();
 if (!typeCd) {
 alert('문제유형을 선택해주세요.');
 return false;
 }
 if (!content) {
 alert('문제를 입력해주세요.');
 return false;
 }
 if (!answer) {
 alert('정답을 입력해주세요.');
 return false;
 }
 if (!points) {
 alert('포인트를 선택해주세요.');
 return false;
 }
 if (!category) {
 alert('카테고리를 선택해주세요.');
 return false;
 }
 if (!levelCd) {
 alert('난이도를 선택해주세요.');
 return false;
 }
 return true;
}
$(document).on('click', '#submitBtn', function() {
 if (!checkField()) {
 return;
 }
 const form = $('#newQuizForm');
 const formData = form.serialize();

 //객관식일 경우 정답 지정 여부 확인
 if ($('#choices-wrapper').length > 0 && form.find('input[name="answer"]:checked').length === 0) {
     alert('정답을 지정해주세요.');
     form.find('input[name="answer"]').first().focus();
     return;
   }

 $.ajax({
 url: '/wiki/register',
 type: 'POST',
 data: formData,
 success: function() {
 alert('퀴즈 등록이 완료되었습니다.');
 window.location.href = '/wiki';
 },
 error: function() {
 alert('퀴즈 등록에 실패했습니다.');
 }
 });
});
