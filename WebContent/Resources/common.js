function deleteArticle(id, memberId) {

	if (isLogined && memberId == loginedMemberId) {
		if (confirm('삭제하시겠습니까?')) {
			location.replace('./doDelete.sbs?id=' + id);
		}

		return;
	}

	$('.article-delete-popup,.popup-bg').css('display', 'block');

	var $popup = $('.article-delete-popup');

	$popup.find('input[name="id"]').val(id);

	$popup.find('input[name="passwd"]').val('');

	$popup.find('input[name="passwd"]').focus();

}

function deleteArticleReply(id, memberId) {

	if (isLogined && memberId == loginedMemberId) {
		if (confirm('삭제하시겠습니까?')) {
			location.replace('./doDeleteReply.sbs?id=' + id);
		}
		return;
	}

	$('.article-reply-delete-popup,.popup-bg').css('display', 'block');

	var $popup = $('.article-reply-delete-popup');

	$popup.find('input[name="id"]').val(id);

	$popup.find('input[name="passwd"]').val('');

	$popup.find('input[name="passwd"]').focus();

}

$(function() {

	$('.popup-bg, .popup > .head > .btn-close, .popup input[type="reset"]')

	.click(function() {

		$('.popup, .popup-bg').css('display', 'none');

	});

});