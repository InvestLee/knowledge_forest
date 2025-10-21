$(function() {

 console.log(document.getElementById("isAdmin").value);
 if(document.getElementById("isAdmin").value === "false"){
 alert("관리자가 아닙니다.");
 history.back();
 }

 function loadQuizList(type, page = 0) {
 let url = `/admin/${type.toLowerCase()}?page=${page}`;
 $('#quiz-list-section').load(url);
 }

 // 초기에 등록된 문제 로드
 loadQuizList('PENDING');

 // 탭 클릭 시 처리
 $('#quizTab .custom-tab-button').on('click', function() {
 $('#quizTab .custom-tab-button').removeClass('active');
 $(this).addClass('active');

 const type = $(this).data('type');
 loadQuizList(type);
 });

 // 페이징 처리도 기존처럼 클래스 바인딩해서 동작하게 할 수 있음
 $(document).on('click', '.approved-page-link, .pending-page-link, .rejected-page-link', function(e) {
 e.preventDefault();
 const page = $(this).data('page');
 const type = $(this).hasClass('approved-page-link') ? 'APPROVED' :
 $(this).hasClass('pending-page-link') ? 'PENDING' : 'REJECTED';

 loadQuizList(type, page);
 });
});

// 승인 버튼 클릭
$(document).on('click', '#approveBtn', function() {
 const form = $('#approveForm');
 const formData = form.serialize();

 //객관식일 경우 정답 지정 여부 확인
 if ($('#choices-wrapper').length > 0 && form.find('input[name="answer"]:checked').length === 0) {
     alert('정답을 지정해주세요.');
     form.find('input[name="answer"]').first().focus();
     return;
   }

 $.ajax({
 url: '/admin/approve',
 type: 'POST',
 data: formData,
 success: function() {
 alert('승인 완료');
 $('#quizDetailModal').modal('hide');
 newLoadPending();
 },
 error: function() {
 alert('승인 처리 실패');
 }
 });
});

// 반려 버튼 클릭
$(document).on('click', '#rejectBtn', function() {
 const form = $('#rejectForm');
 const formData = form.serialize();

 $.ajax({
 url: '/admin/reject',
 type: 'POST',
 data: formData,
 success: function() {
 alert('반려 완료');
 $('#quizDetailModal').modal('hide');
 newLoadPending();
 },
 error: function() {
 alert('반려 처리 실패');
 }
 });
});

// 수정 버튼 클릭
$(document).on('click', '#updateBtn', function() {
 const form = $('#updateForm');
 const formData = form.serialize();

 //객관식일 경우 정답 지정 여부 확인
 if ($('#choices-wrapper').length > 0 && form.find('input[name="answer"]:checked').length === 0) {
     alert('정답을 지정해주세요.');
     form.find('input[name="answer"]').first().focus();
     return;
   }

 $.ajax({
 url: '/admin/update',
 type: 'POST',
 data: formData,
 success: function() {
 alert('수정 완료');
 $('#quizDetailModal').modal('hide');
 newLoadApproved()
 },
 error: function() {
 alert('수정 실패');
 }
 });
});

// 삭제 버튼 클릭
$(document).on('click', '#removeBtn', function() {
 const form = $('#updateForm');
 const formData = form.serialize();

 $.ajax({
 url: '/admin/remove',
 type: 'POST',
 data: formData,
 success: function() {
 alert('삭제 완료');
 $('#quizDetailModal').modal('hide');
 newLoadApproved();
 },
 error: function() {
 alert('삭제 실패');
 }
 });
});

$(document).on('click', '.quiz-row', function() {
 const questionNo = $(this).data('question-no');
 if (!questionNo) return;

 $.ajax({
 url: `/admin/question/detail/${questionNo}`,
 method: 'GET',
 success: function(html) {
 $('#quiz-detail-body').html(html);
 const modal = new bootstrap.Modal(document.getElementById('quizDetailModal'));
 modal.show();
 },
 error: function() {
 alert('문제 상세 조회에 실패했습니다.');
 }
 });
});

$(document).on('click', '.rejected-quiz-row', function() {
 const questionNo = $(this).data('question-no');
 if (!questionNo) return;

 $.ajax({
 url: `/admin/question/rejected/detail/${questionNo}`,
 method: 'GET',
 success: function(html) {
 $('#quiz-detail-body').html(html);
 const modal = new bootstrap.Modal(document.getElementById('quizDetailModal'));
 modal.show();
 },
 error: function() {
 alert('문제 상세 조회에 실패했습니다.');
 }
 });
});
$(document).on('click', '.approved-quiz-row', function() {
 const questionNo = $(this).data('question-no');
 if (!questionNo) return;

 $.ajax({
 url: `/admin/question/approved/detail/${questionNo}`,
 method: 'GET',
 success: function(html) {
 $('#quiz-detail-body').html(html);
 const modal = new bootstrap.Modal(document.getElementById('quizDetailModal'));
 modal.show();
 },
 error: function() {
 alert('문제 상세 조회에 실패했습니다.');
 }
 });
});

function newLoadPending() {
    $('#quiz-list-section').load('/admin/pending?page=0');
}

function newLoadApproved() {
    $('#quiz-list-section').load('/admin/approved?page=0');
}

// 현재 DOM에서 choices 인덱스의 최대값 찾기 (choices[3].choiceNo → 3)
function getNextIndex() {
  const els = document.querySelectorAll('#choices-wrapper input[name$=".choiceNo"]');
  let maxIdx = -1;
  els.forEach(el => {
    const m = el.name.match(/choices\[(\d+)]\.choiceNo/);
    if (m) {
      const idx = parseInt(m[1], 10);
      if (!isNaN(idx) && idx > maxIdx) maxIdx = idx;
    }
  });
  return maxIdx + 1; // 다음 인덱스
}

// 현재 DOM에서 화면 번호(choiceNo)의 최대값 찾기 → 다음 번호
function getNextNo() {
  const els = document.querySelectorAll('#choices-wrapper input[name$=".choiceNo"]');
  let maxNo = 0;
  els.forEach(el => {
    const n = parseInt(el.value, 10);
    if (!isNaN(n) && n > maxNo) maxNo = n;
  });
  return maxNo + 1;
}

$(document).on('click', '#add-choice-btn', function () {
  const wrapper = $('#choices-wrapper');

  // 지금 개수 기준으로 새 항목의 표시번호/인덱스 계산
  const nextIndex = wrapper.find('.input-group').length;
  const nextNo = nextIndex + 1;

  const newChoice = `
    <div class="input-group mb-2">
      <span class="input-group-text">${nextNo}</span>

      <input type="hidden" class="form-control"
             name="choices[${nextIndex}].choiceNo"
             value="${nextNo}">

      <input type="text" class="form-control"
             name="choices[${nextIndex}].content"
             placeholder="보기 ${nextNo} 입력" required>

      <div class="input-group-text">
        <input type="radio" name="answer" value="${nextNo}"> 정답
      </div>

      <button type="button" class="btn btn-outline-danger btn-sm ms-2 remove-choice-btn">삭제</button>
    </div>
  `;
  wrapper.append(newChoice);

  // (옵션) 표시용 hidden 갱신
  const maxEl = document.getElementById('maxChoiceNo');
  if (maxEl) maxEl.value = String(nextNo);
});

// 선택지 삭제
$(document).on('click', '.remove-choice-btn', function () {
  $(this).closest('.input-group').remove();
  reindexChoices(); // 삭제 후 반드시 재인덱싱
});

function reindexChoices() {
  const $groups = $('#choices-wrapper .input-group');

  $groups.each(function (i) {
    const displayNo = i + 1;
    const $group = $(this);

    // 1) 화면 번호 갱신
    $group.find('.input-group-text').first().text(displayNo);

    // 2) hidden: choices[i].choiceNo + 값(표시번호)
    const $hidden = $group.find('input[type="hidden"][name$=".choiceNo"]');
    if ($hidden.length) {
      $hidden.attr('name', `choices[${i}].choiceNo`).val(displayNo);
    } else {
      // 혹시 기존 항목에 hidden이 없다면 보강
      $group.prepend(
        `<input type="hidden" class="form-control" name="choices[${i}].choiceNo" value="${displayNo}">`
      );
    }

    // 3) text input: choices[i].content + placeholder
    $group.find('input[type="text"]').attr({
      name: `choices[${i}].content`,
      placeholder: `보기 ${displayNo} 입력`
    });

    // 4) 라디오 value(정답 값)도 표시번호로 맞춤
    const $radio = $group.find('input[type="radio"][name="answer"]');
    const wasChecked = $radio.prop('checked'); // 체크 유지용
    $radio.val(displayNo);
    if (wasChecked) $radio.prop('checked', true); // 유지
  });

  // 총 개수 갱신(옵션)
  $('#maxChoiceNo').val(String($groups.length));
}
