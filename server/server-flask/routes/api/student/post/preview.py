from flask import Response
from flask_restful_swagger_2 import Resource, swagger

from db.models.post import FAQModel, NoticeModel, RuleModel
from routes.swagger_docs.student import preview_doc
from support import post_inquire_helper


class FAQPreview(Resource):
    @swagger.doc(preview_doc.FAQ_PREVIEW_GET)
    def get(self):
        """
        FAQ 프리뷰 조회
        """
        faq = FAQModel.objects(pinned=True).first()
        if not faq:
            faq = FAQModel.objects().first()
            if not faq:
                return Response('', 204)

        return post_inquire_helper.inquire(FAQModel, faq.id), 200


class NoticePreview(Resource):
    @swagger.doc(preview_doc.NOTICE_PREVIEW_GET)
    def get(self):
        """
        공지사항 프리뷰 조회
        """
        notice = NoticeModel.objects(pinned=True).first()
        if not notice:
            notice = NoticeModel.objects().first()
            if not notice:
                return Response('', 204)

        return post_inquire_helper.inquire(NoticeModel, notice.id), 200


class RulePreview(Resource):
    @swagger.doc(preview_doc.RULE_PREVIEW_GET)
    def get(self):
        """
        기숙사규정 프리뷰 조회
        """
        rule = RuleModel.objects(pinned=True).first()
        if not rule:
            rule = RuleModel.objects().first()
            if not rule:
                return Response('', 204)

        return post_inquire_helper.inquire(RuleModel, rule.id), 200