window.addEventListener("DOMContentLoaded", function() {
	/** 체크 박스 전체 선택 처리 S */
	const checkAllEls = document.getElementsByClassName("check_all");
	for (let el of checkAllEls) {
		el.addEventListener("click", function() {
			const targetName = this.dataset.targetName;
			const els = document.getElementsByName(targetName);
			for (const _el of els) {
				_el.checked = this.checked;
			}
		});
	}
	/** 체크 박스 전체 선택 처리 E */
}); 