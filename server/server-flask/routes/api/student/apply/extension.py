from datetime import datetime, time

from flask import Response
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful_swagger_2 import Resource, request, swagger

from db.models.account import StudentModel
from db.models.apply import ExtensionApplyModel
from routes.swagger_docs.student import extension_doc

APPLY_START = time(17, 30)
APPLY_END = time(22, 0)


class Extension(Resource):
    @swagger.doc(extension_doc.EXTENSION_GET)
    @jwt_required
    def get(self):
        """
        연장신청 정보 조회
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()

        if not student.extension_apply:
            return Response('', 204)

        return {
            'class': student.extension_apply.class_,
            'seat': student.extension_apply.seat
        }, 200

    @swagger.doc(extension_doc.EXTENSION_POST)
    @jwt_required
    def post(self):
        """
        연장신청
        """
        now = datetime.now().time()

        if not APPLY_START < now < APPLY_END:
            return Response('', 204)

        class_ = request.form.get('class', type=int)
        seat = request.form.get('seat', type=int)

        student = StudentModel.objects(id=get_jwt_identity()).first()
        student.update(extension_apply=ExtensionApplyModel(class_=class_, seat=seat))

        return Response('', 201)