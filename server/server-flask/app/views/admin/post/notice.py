from flask import Response
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource, request
from flasgger import swag_from

from app.docs.admin.post.notice import *
from app.models.account import AdminModel
from app.models.post import NoticeModel


class AdminNotice(Resource):
    uri = '/admin/notice'

    @swag_from(NOTICE_POST)
    @jwt_required
    def post(self):
        """
        공지사항 업로드
        """
        admin = AdminModel.objects(id=get_jwt_identity()).first()
        if not admin:
            return Response('', 403)

        title = request.form.get('title')
        content = request.form.get('content')

        NoticeModel(title = title, content = content, author = admin).save()

        return Response('', 201)

    @swag_from(NOTICE_PATCH)
    @jwt_required
    def patch(self):
        """
        공지사항 내용 수정
        """
        admin = AdminModel.objects(id=get_jwt_identity()).first()
        if not admin:
            return Response('', 403)

        id = request.form.get('id')
        title = request.form.get('title')
        content = request.form.get('content')

        post = NoticeModel.objects(id=id).first()
        post.update(title=title, content=content)

        return Response('', 200)

    @swag_from(NOTICE_DELETE)
    @jwt_required
    def delete(self):
        """
        공지사항 제거
        """
        admin = AdminModel.objects(id=get_jwt_identity()).first()
        if not admin:
            return Response('', 403)

        id = request.form.get('id')

        NoticeModel.objects(id=id).first().delete()

        return Response('', 200)
