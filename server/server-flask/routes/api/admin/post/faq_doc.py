FAQ_POST = {
    'tags': ['게시글'],
    'description': 'FAQ 업로드',
    'parameters': [
        {
            'name': 'Authorization',
            'description': 'JWT Token',
            'in': 'header',
            'type': 'str'
        },
        {
            'name': 'title',
            'description': 'FAQ 제목',
            'in': 'formData',
            'type': 'str'
        },
        {
            'name': 'content',
            'description': 'FAQ 내용',
            'in': 'formData',
            'type': 'str'
        }
    ],
    'responses': {
        '201': {
            'description': 'FAQ 업로드 성공'
        }
    }
}

FAQ_PATCH = {
    'tags': ['게시글'],
    'description': 'FAQ 수정',
    'parameters': [
        {
            'name': 'Authorization',
            'description': 'JWT Token',
            'in': 'header',
            'type': 'str'
        },
        {
            'name': 'id',
            'description': '수정할 FAQ ID',
            'in': 'formData',
            'type': 'str'
        },
        {
            'name': 'title',
            'description': 'FAQ 제목',
            'in': 'formData',
            'type': 'str'
        },
        {
            'name': 'content',
            'description': 'FAQ 내용',
            'in': 'formData',
            'type': 'str'
        }
    ],
    'responses': {
        '200': {
            'description': '수정 성공'
        }
    }
}

FAQ_DELETE = {
    'tags': ['게시글'],
    'description': 'FAQ 삭제',
    'parameters': [
        {
            'name': 'Authorization',
            'description': 'JWT Token',
            'in': 'header',
            'type': 'str'
        },
        {
            'name': 'id',
            'description': '삭제할 FAQ ID',
            'in': 'formData',
            'type': 'str'
        }
    ],
    'responses': {
        '200': {
            'description': '삭제 성공'
        }
    }
}
