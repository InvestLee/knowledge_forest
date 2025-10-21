// -------------------------------------- 로그인 프로세스 --------------------------------------
function validateEncryptedForm() {
    var username = document.getElementById("imsi_id").value;
    var password = document.getElementById("imsi_password").value;
    if (!username || !password) {
        alert("ID/비밀번호를 입력해주세요.");
        return false;
    }

    try {
        var rsaPublicKeyModulus = document.getElementById("rsaPublicKeyModulus").value;
        var rsaPublicKeyExponent = document.getElementById("rsaPublicKeyExponent").value;
        submitEncryptedForm(username,password,  rsaPublicKeyModulus, rsaPublicKeyExponent);
    } catch(err) {
        alert(err);
    }
    return false;
}

function hexToBytes(hex) {
  const bytes = [];
  for (let c = 0; c < hex.length; c += 2) {
    bytes.push(parseInt(hex.substr(c, 2), 16));
  }
  return bytes;
}

function hexToBase64(hex) {
  const bytes = hexToBytes(hex);
  const binary = bytes.map(b => String.fromCharCode(b)).join('');
  return btoa(binary);
}

function submitEncryptedForm(username, password, rsaPublicKeyModulus, rsaPpublicKeyExponent) {
    var rsa = new RSAKey();
    rsa.setPublic(rsaPublicKeyModulus, rsaPpublicKeyExponent);

    // 사용자ID와 비밀번호를 RSA로 암호화한다.
 var encryptedUsernameHex = rsa.encrypt(username);
 var encryptedPasswordHex = rsa.encrypt(password);

 //RSA로 암호화한 hex 결과를 Base64로 변환
 var securedUsername = hexToBase64(encryptedUsernameHex);
 var securedPassword = hexToBase64(encryptedPasswordHex);

    // POST 로그인 폼에 값을 설정하고 발행(submit) 한다.
    var securedLoginForm = document.getElementById("securedLoginForm");
    securedLoginForm.eno.value = securedUsername;
    securedLoginForm.pwd.value = securedPassword;
    securedLoginForm.submit();
}


//-------------------------------------- 로그인 여부 체크 --------------------------------------
function checkLogin() {
 var isLogin = false;
  $.ajax({
 url : 'isLogin.do',
 dataType : 'json',
 async : false,
 success : function(data) {
 // isLogin 값이 "Y"이면 로그인 되어 있는 상태
 var result = data.isLogin;
 if (result == "Y") {
 isLogin = true;
 }
 }
 });

 return isLogin;
}


document.addEventListener("DOMContentLoaded", function () {
  const f = document.forms["response"];
  if (f && f.err) {
    const errVal = f.err.value;
    switch (errVal) {
      case "error":
        alert("비밀번호가 일치하지 않습니다.");
        location.href = "/login";
        break;
      case "NotInOffice":
        alert("재직중이 아닙니다.");
        location.href = "/login";
        break;
      case "noinfo":
        alert("사용자 정보가 없습니다.");
        location.href = "/login";
        break;
    }
  }

  const loginForm = document.forms["securedLoginForm"];
  if (
    loginForm &&
    loginForm.imsi_id &&
    loginForm.imsi_password &&
    loginForm.imsi_id.value !== "" &&
    loginForm.imsi_password.value !== ""
  ) {
    validateEncryptedForm();
  }
});

function Upper(e, r) {
  if (r && r.value !== undefined) {
    r.value = r.value.toUpperCase();
  }
}

document.addEventListener('DOMContentLoaded', () => {
   const url = new URL(window.location.href);
   if (url.searchParams.get('error') === '1') {
     alert('아이디 또는 비밀번호가 잘못되었습니다.');
     url.searchParams.delete('error');
     history.replaceState(null, '', url.pathname + (url.search ? '?' + url.searchParams.toString() : '') + url.hash);
   }
});

let allowModalOpen = false;
const modalEl = document.getElementById('quizDetailModal');

modalEl.addEventListener('show.bs.modal', function (e) {
  if (!allowModalOpen) {
 window.location.href = 'login';
 e.preventDefault(); // ✅ 허용 플래그 없으면 절대 안 뜸
 }
});
