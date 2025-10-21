let currentCategory = '';

$(document).ready(function() {
 // 초기 로딩
 loadQuestions(currentCategory, 0);

 // 카테고리 버튼 클릭 이벤트
 $('.category-btn').on('click', function() {
 currentCategory = $(this).data('category');
 $('.category-btn').removeClass('active');
 $(this).addClass('active');
 loadQuestions(currentCategory, 0);
 });

 // 페이징 클릭 이벤트 (Ajax로 로드된 내용이므로 위임 방식 사용)
 $(document)
 .off('click', '.wiki-page-link')
 .on('click', '.wiki-page-link', function(e) {
 e.preventDefault();
 const page = $(this).data('page');
 loadQuestions(currentCategory, page);
 });

 // Ajax로 wiki 리스트를 로딩하는 함수
 function loadQuestions(category, page) {
 const url = '/wiki/category?page=' + page + '&category=' + encodeURIComponent(category);
 $('#wiki-list-section').load(url);
 }
});

$(document).on('click', '.wiki-row', function() {
 const questionNo = $(this).data('question-no');
 if (!questionNo) return;

 $.ajax({
 url: `/wiki/${questionNo}`,
 method: 'GET',
 success: function(html) {
 $('#quiz-detail-body').html(html);
 const modal = new bootstrap.Modal(document.getElementById('quizDetailModal'));
 modal.show();
 }
 });
});

// 검색 버튼 클릭 이벤트
$('#searchBtn').on('click', function () {
  let currentKeyword = $('#searchKeyword').val().trim();
  const url = '/wiki/category?category=' + encodeURIComponent(currentCategory) +
              '&search=' + encodeURIComponent(currentKeyword);
  $('#wiki-list-section').load(url);
});