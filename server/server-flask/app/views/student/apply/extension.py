from datetime import datetime, time
import json

from flask import Response
from flask_jwt_extended import get_jwt_identity, jwt_required
from flask_restful import Resource, request
from flasgger import swag_from

from app.docs.student.apply.extension import *
from app.models.account import StudentModel
from app.models.apply import ExtensionApplyModel

APPLY_START = time(17, 30)
APPLY_END_11 = time(20, 30)
APPLY_END_12 = time(22, 0)

MAPS = {
    1: [
        [1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1]
    ],
    2: [
        [1, 0, 1, 0, 1, 0, 1],
        [1, 0, 1, 0, 1, 0, 1],
        [1, 0, 1, 0, 1, 0, 1],
        [1, 0, 0, 1, 0, 0, 1],
        [1, 0, 0, 0, 0, 0, 1],
        [0, 0, 1, 1, 1, 0, 0]
    ],
    3: [
        [1, 1, 1, 1],
        [1, 1, 1, 1],
        [1, 1, 1, 1],
        [1, 1, 1, 1],
        [1, 1, 1, 1],
    ],
    4: [
        [1, 1, 0, 0, 1, 1],
        [1, 1, 0, 0, 1, 1],
        [0, 0, 1, 1, 0, 0],
        [0, 0, 1, 1, 0, 0],
        [1, 1, 0, 0, 1, 1],
        [1, 1, 0, 0, 1, 1]
    ],
    5: [
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0]
    ],
    6: [
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 1],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0],
        [1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0]
    ],
    7: [
        [1, 1, 1, 0, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 0, 1, 1, 1],
        [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
        [1, 1, 1, 0, 1, 1, 0, 1, 1, 1],
        [1, 1, 1, 0, 1, 1, 0, 1, 1, 1],
        [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
        [1, 1, 1, 0, 0, 1, 0, 0, 0, 0],
        [1, 1, 1, 0, 0, 0, 0, 0, 0, 0],
        [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 1, 0, 0, 0, 0, 0, 0, 0],
        [1, 1, 1, 0, 0, 0, 0, 0, 0, 0]
    ]
}


class Extension11(Resource):
    uri = '/extension/11'

    @swag_from(EXTENSION_GET)
    @jwt_required
    def get(self):
        """
        11시 연장신청 정보 조회
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        if not student.extension_apply_11:
            return Response('', 204)

        return {
            'class': student.extension_apply_11.class_,
            'seat': student.extension_apply_11.seat
        }, 200

    @swag_from(EXTENSION_POST)
    @jwt_required
    def post(self):
        """
        11시 연장신청
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        now = datetime.now().time()

        if not APPLY_START < now < APPLY_END_11:
            return Response('', 204)

        class_ = int(request.form['class'])
        seat = int(request.form['seat'])

        student.update(extension_apply_11=ExtensionApplyModel(class_=class_, seat=seat))

        return Response('', 201)


class Extension12(Resource):
    uri = '/extension/12'

    @swag_from(EXTENSION_GET)
    @jwt_required
    def get(self):
        """
        12시 연장신청 정보 조회
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        if not student.extension_apply_12:
            return Response('', 204)

        return {
            'class': student.extension_apply_12.class_,
            'seat': student.extension_apply_12.seat
        }, 200

    @swag_from(EXTENSION_POST)
    @jwt_required
    def post(self):
        """
        12시 연장신청
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        now = datetime.now().time()

        if not APPLY_START < now < APPLY_END_12:
            return Response('', 204)

        class_ = int(request.form['class'])
        seat = int(request.form['seat'])

        student.update(extension_apply_12=ExtensionApplyModel(class_=class_, seat=seat))

        return Response('', 201)


class ExtensionMap11(Resource):
    uri = '/extension/map/11'

    @swag_from(EXTENSION_MAP_GET)
    @jwt_required
    def get(self):
        """
        11시 연장신청 지도 조회
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        class_ = int(request.args['class'])

        map_ = MAPS[class_]

        applied_students = {student.extension_apply_11.seat: student.name for student in StudentModel.objects() if student.extension_apply_11 and student.extension_apply_11.class_ == class_}

        seat_count = 1

        for i, row in enumerate(map_):
            for j, seat in enumerate(row):
                if map_[i][j]:
                    if seat_count in applied_students:
                        map_[i][j] = applied_students[seat_count]
                    else:
                        map_[i][j] = seat_count

                    seat_count += 1

        return Response(json.dumps(map_, ensure_ascii=False), 200, content_type='application/json; charset=utf8')


class ExtensionMap12(Resource):
    uri = '/extension/map/12'

    @swag_from(EXTENSION_MAP_GET)
    @jwt_required
    def get(self):
        """
        12시 연장신청 지도 조회
        """
        student = StudentModel.objects(id=get_jwt_identity()).first()
        if not student:
            return Response('', 403)

        class_ = int(request.args['class'])

        map_ = MAPS[class_]

        applied_students = {student.extension_apply_12.seat: student.name for student in StudentModel.objects() if student.extension_apply_12 and student.extension_apply_12.class_ == class_}

        seat_count = 1

        for i, row in enumerate(map_):
            for j, seat in enumerate(row):
                if map_[i][j]:
                    if seat_count in applied_students:
                        map_[i][j] = applied_students[seat_count]
                    else:
                        map_[i][j] = seat_count

                    seat_count += 1

        return Response(json.dumps(map_, ensure_ascii=False), 200, content_type='application/json; charset=utf8')
