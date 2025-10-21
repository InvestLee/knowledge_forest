document.addEventListener("DOMContentLoaded", function () {
  const eno = document.body.dataset.eno;

  function fetchSolvedList(page = 0) {
    const payload = {
      correctYn: "Y",
      category: document.getElementById("category").value,
      lev: document.getElementById("lev").value,
      type: document.getElementById("type").value,
      keyword: document.getElementById("keyword").value,
      page: page,
      size: 20
    };
 console.log(payload);
    fetch("/mypage/solved", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    })
      .then(res => res.json())
      .then(data => {
        const tbody = document.getElementById("solvedResult");
        tbody.innerHTML = "";

        if (data.content.length === 0) {
          tbody.innerHTML = `<tr><td colspan="7">검색 결과가 없습니다.</td></tr>`;
          renderPagination(0, 0); // 빈 페이지네이션도 초기화
          return;
        }

        data.content.forEach(p => {
          const tr = document.createElement("tr");
          tr.innerHTML = `
            <td>${p.question_no}</td>
            <td>${p.content}</td>
            <td>${p.category_name}</td>
            <td>${p.type}</td>
            <td>${p.lev}</td>
            <td>${p.points}</td>
            <td>${p.lt_ch_dtti}</td>
          `;
          tbody.appendChild(tr);
        });

        renderPagination(data.number, data.total_pages);
      });
  }

  function renderPagination(currentPage, totalPages) {
    const pagination = document.querySelector("#pagination");
    pagination.innerHTML = "";

    if (totalPages == 0) return;

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
        fetchSolvedList(page);
      });
    });
  }

  // ✅ 검색 버튼 클릭 시
  document.getElementById("searchBtn").addEventListener("click", function () {
    fetchSolvedList(0);
  });

  // ✅ 엔터로 검색하는 경우도 대응
  document.querySelector(".search-form").addEventListener("submit", function (e) {
    e.preventDefault();
    fetchSolvedList(0);
  });

  // ✅ 초기 로딩
  fetchSolvedList();
});