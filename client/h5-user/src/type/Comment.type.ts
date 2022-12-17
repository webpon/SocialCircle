export default interface CommentType {
  dynamicId?: number;
  createTime?: number;
  id?: number;
  userId?: number;
  content?: string;
  parentId?: number;
  likeNum?: number;
  childList?: Array<CommentType>
}
