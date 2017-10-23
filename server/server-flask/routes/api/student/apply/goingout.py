from flask_restful import Resource, request
from flask_jwt import current_identity, jwt_required

from db.models.account import StudentModel
from db.models.apply import GoingoutApplyModel


class Goingout(Resource):
    @jwt_required()
    def get(self):
        student = StudentModel.objects(id=current_identity).first()

        return {
            'sat': student.goingout_applies.on_saturday,
            'sun': student.goingout_applies.on_sunday
        }, 200

    @jwt_required()
    def post(self):
        sat = request.form.get('sat', type=bool)
        sun = request.form.get('sun', type=bool)
        student = StudentModel.objects(id=current_identity).first()
        student.update(goingout_applies=GoingoutApplyModel(on_saturday=sat, on_sunday=sun))

        return '', 201