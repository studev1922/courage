package courage.model.util;

public interface HtmlTemp {
    static String emailCode(String code) {
        return new StringBuilder()
                .append("<form><div style='display:flex;justify-content: space-between;align-items: end'><h3><a href='https://github.com/studev1922' style='text-decoration:none' target='_blank'><img width='64px' src='https://avatars.githubusercontent.com/u/131136059' alt='courage' style='border-radius:25%'><span style='color:#006aff;text-shadow:0 0 1px #000'>Studev Courage</span></a></h3><div><a href='https://www.youtube.com/channel/UCzlBNfBsCNFrdcJ3OxyGHsQ'><img src='' alt='youtube'></a><a href=''><img src='' alt='facebook'></a><a href='https://www.linkedin.com/in/hoa-duy-5750b4273/'><img src='' alt='linkin'></a></div></div><hr><h3 style='text-shadow:0 0 1px #000'><a href='http://localhost:8080/index.html' target='_blank'><em style='color:#006aff'>Expansive System</em></a> x\u00E1c nh\u1EADn email \u0111\u0103ng k\u00FD t\u00E0i kho\u1EA3n</h3><div style='display:flex; align-items: center;'>H\u1EC7 th\u1ED1ng nh\u1EADn \u0111\u01B0\u1EE3c y\u00EAu c\u1EA7u cung c\u1EA5p m\u00E3 x\u00E1c th\u1EF1c email<span style='margin:.25rem;font-weight:700;padding:.5rem 1rem;border-radius:.25rem;border:1px solid orange;font-size:1.2em'>")
                .append(code)
                .append("</span></div><p><em>C\u1EA3m \u01A1n b\u1EA1n \u0111\u00E3 s\u1EED d\u1EE5ng d\u1ECBch v\u1EE5 c\u1EE7a Expansive System, \u0111\u00E2y l\u00E0 email \u0111\u01B0\u1EE3c g\u1EEDi t\u1EEB h\u1EC7 th\u1ED1ng vui l\u00F2ng kh\u00F4ng ph\u1EA3n h\u1ED3i.</em></p><h5>Ch\u00FAc b\u1EA1n m\u1ED9t ng\u00E0y vui v\u1EBB.</h5></form>")
                .toString();
    }
}
