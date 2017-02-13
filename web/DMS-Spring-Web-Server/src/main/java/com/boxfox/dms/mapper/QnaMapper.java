package com.boxfox.dms.mapper;

import com.boxfox.dms.board.dto.Comment;
import com.boxfox.dms.board.dto.QnaPostContext;

public interface QnaMapper extends BoardMapper<QnaPostContext> {
	public void writeAnswer(QnaPostContext post);
	public void writeComment(Comment comment);
	public void editAnswer(QnaPostContext post);
	public void editComment(Comment comment);
}
