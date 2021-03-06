import json

from flask import Response
from flask_restful import Resource
from flasgger import swag_from

from app.docs.student.post.notice import *
from app.models.post import NoticeModel
from support import post_inquire_helper


class NoticeList(Resource):
    uri = '/notice'

    @swag_from(NOTICE_LIST_GET)
    def get(self):
        """
        공지사항 목록 조회
        """
        notice_list = post_inquire_helper.list(NoticeModel)

        return Response(json.dumps(notice_list, ensure_ascii=False), 200, content_type='application/json; charset=utf8')


class Notice(Resource):
    uri = '/notice/<id>'

    @swag_from(NOTICE_GET)
    def get(self, id):
        """
        공지사항 내용 조회
        """
        notice = post_inquire_helper.inquire(NoticeModel, id)

        return Response(json.dumps(notice, ensure_ascii=False), 200, content_type='application/json; charset=utf8') if notice else Response('', 204)
