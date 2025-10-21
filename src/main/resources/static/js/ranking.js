let currentSearchParams = {
  cgp: "",
  unit: "",
  keyword: ""
};

document.addEventListener("DOMContentLoaded", function () {
  const searchBtn = document.getElementById("searchBtn");

  // 검색 버튼 클릭 이벤트
  searchBtn.addEventListener("click", function () {
    // 검색 조건 업데이트
    currentSearchParams = {
      cgp: document.getElementById("cgp").value,
      unit: document.getElementById("unit").value,
      keyword: document.getElementById("nameKeyword").value
    };
    fetchRankingList(0); // 검색 시 1페이지부터
  });

  // 초기 로딩 시
  fetchRankingList(0);
});

function fetchRankingList(page = 0) {
  const payload = {
    page: page,
    size: 20,
    ...currentSearchParams // 현재 검색 조건 포함
  };

  fetch("/rank", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  })
    .then(res => res.json())
    .then(data => {
      const tbody = document.getElementById("rankingBody");
      tbody.innerHTML = "";

      if (!data.content || data.content.length === 0) {
        tbody.innerHTML = `<tr><td colspan="4">검색 결과가 없습니다.</td></tr>`;
        renderPagination(0, 0); // 페이지 없음 처리
        return;
      }

      // 테이블 채우기
      data.content.forEach(r => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
          <td>${r.rank}</td>
          <td>${r.emp_nm}</td>
          <td>${r.solved_count}</td>
          <td>${r.points}</td>
        `;
        tbody.appendChild(tr);
      });

      renderPagination(data.number, data.total_pages);
    });
}

function renderPagination(currentPage, totalPages) {
  const pagination = document.getElementById("pagination");
  pagination.innerHTML = "";

  if (totalPages <= 1) return; // 1페이지 이하면 버튼 숨김

  if (currentPage > 0) {
    pagination.innerHTML += `<a href="#" class="pagination-link" data-page="${currentPage - 1}">&laquo; 이전</a>`;
  }

  for (let i = 0; i < totalPages; i++) {
    pagination.innerHTML += `<a href="#" class="pagination-link ${i === currentPage ? 'active' : ''}" data-page="${i}">${i + 1}</a>`;
  }

  if (currentPage < totalPages - 1) {
    pagination.innerHTML += `<a href="#" class="pagination-link" data-page="${currentPage + 1}">다음 &raquo;</a>`;
  }

  document.querySelectorAll(".pagination-link").forEach(link => {
    link.addEventListener("click", e => {
      e.preventDefault();
      const page = parseInt(link.dataset.page);
      fetchRankingList(page); // 검색 조건 유지
    });
  });
}