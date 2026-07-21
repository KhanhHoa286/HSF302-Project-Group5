
function loadDistrict(provinceId,preSelectedUnitId) {

    const unitSelect = document.getElementById("administrativeUnitId");
    let htmlDistrictOption;
    if(provinceId === null || provinceId === "") {
        htmlDistrictOption += '<option value="" disabled selected>Chọn quận/huyện</option>';
        unitSelect.innerHTML = htmlDistrictOption;
        return;
    }
    const savedIdStr = preSelectedUnitId ? String(preSelectedUnitId).trim() : null;
    axios.get(`/api/recruiter/load-district?province_id=${provinceId}`)
        .then(response =>{
            const data = response.data;
            htmlDistrictOption = '<option value="" disabled selected>Chọn quận/huyện</option>';
            data.forEach(district => {
                const currentIdStr = String(district.unitId).trim();
                const isSelected = (savedIdStr && currentIdStr === savedIdStr) ? 'selected' : '';
                htmlDistrictOption += `<option value="${district.unitId}" ${isSelected}>${district.unitName}</option>`;
            })
            unitSelect.innerHTML = htmlDistrictOption;
        })
        .catch(error => {
            console.log("Không tìm được mã tỉnh!", error);
        })
}

document.addEventListener("DOMContentLoaded", function () {
    const provinceSelect = document.getElementById("provinceId");

    if (provinceSelect) {
        const currentProvinceId = provinceSelect.value;
        const preSelectedUnitId = provinceSelect.getAttribute("data-saved-district");

        if (currentProvinceId) {
            loadDistrict(currentProvinceId, preSelectedUnitId);
        }
    }
});