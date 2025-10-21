window.addEventListener('DOMContentLoaded', () => {
  const raw = document.getElementById('categoryData').textContent;
  const categoryData = JSON.parse(raw);


  const labels = categoryData.map(c => c.categoryName);
  const counts = categoryData.map(c => c.solvedCount);
  const colors = [
    "#8BC34A", "#FF9800", "#03A9F4", "#E91E63", "#9C27B0",
    "#FFEB3B", "#795548", "#00BCD4", "#CDDC39", "#FF5722"
  ];

  const ctx = document.getElementById('categoryPieChart').getContext('2d');

  const total = counts.reduce((a, b) => a + b, 0);
  let startAngle = 0;

  counts.forEach((count, i) => {
    const sliceAngle = (count / total) * 2 * Math.PI;
    ctx.beginPath();
    ctx.moveTo(160, 160);
    ctx.arc(160, 160, 140, startAngle, startAngle + sliceAngle);
    ctx.closePath();
    ctx.fillStyle = colors[i % colors.length];
    ctx.fill();

 // 중앙 각도
    const midAngle = startAngle + sliceAngle / 2;
    const labelX = 160 + Math.cos(midAngle) * 80;
    const labelY = 160 + Math.sin(midAngle) * 80;

    ctx.fillStyle = "#ffffff";
    ctx.font = "bold 12px sans-serif";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";
    ctx.fillText(labels[i], labelX, labelY);

    startAngle += sliceAngle;
  });

  // 범례
  const legendTbody = document.querySelector("#categoryLegendTable tbody");
  legendTbody.innerHTML = ""; // 초기화

  categoryData.forEach((c, i) => {
    const row = document.createElement("tr");

    const colorCell = document.createElement("td");
    const colorBox = document.createElement("div");
    colorBox.style.width = "12px";
    colorBox.style.height = "12px";
    colorBox.style.backgroundColor = colors[i % colors.length];
    colorBox.style.borderRadius = "2px";
    colorBox.style.border = "1px solid #ccc";
    colorBox.style.display = "inline-block";

    colorCell.appendChild(colorBox);

    const nameCell = document.createElement("td");
    nameCell.textContent = c.categoryName;

    const countCell = document.createElement("td");
    countCell.textContent = c.solvedCount;

    row.appendChild(colorCell);
    row.appendChild(nameCell);
    row.appendChild(countCell);

    legendTbody.appendChild(row);
  });
});

function getMaxColumns() {
  const containerWidth = document.getElementById('streakGrid').clientWidth || window.innerWidth;
  const cellWidth = 28; // 셀 너비 + 간격 여유
  return Math.floor(containerWidth / cellWidth);
}

function renderStreaks(data) {
  const grid = document.getElementById('streakGrid');
  grid.innerHTML = '';

  const maxColumns = Math.floor((grid.clientWidth || window.innerWidth) / 28);
  const maxItems = Math.min(maxColumns * 7, 365);

  const sliced = data.slice(-maxItems); // 최신 데이터 기준 자름

  sliced.forEach(s => {
    const div = document.createElement('div');
    div.className = 'streak-icon';
    div.textContent =
      s.solvedCount >= 7 ? '🌳' :
      s.solvedCount >= 5 ? '🌲' :
      s.solvedCount >= 3 ? '🌿' :
      s.solvedCount > 0  ? '🌱' : '🥜'
    div.setAttribute('data-tooltip', `${s.solveDate} ${s.solvedCount}문제 해결`);
    grid.appendChild(div);
  });
}

function scrollToLatest() {
  const grid = document.getElementById('streakGrid');
  grid.scrollLeft = grid.scrollWidth;
}

  document.addEventListener("DOMContentLoaded", () => {
    const raw = document.getElementById('streakData').textContent;
    const streakData = JSON.parse(raw);
    renderStreaks(streakData); // ⬅ 바로 호출!
    scrollToLatest(); //  바로 최신 스트릭 보이게
  });

  // 리사이즈 대응
  window.addEventListener("resize", () => {
    const raw = document.getElementById('streakData').textContent;
    const streakData = JSON.parse(raw);
    renderStreaks(streakData);
    scrollToLatest();
  });
