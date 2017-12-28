package cn.liuyin.manhua.data;

public class Book
{
	public String title;
	public int bookid;
	public int chapterId;
	public String code;
	public int count;
	public int index;

	@Override
	public String toString()
	{
		// TODO: Implement this method
		return "title="+this.title+"bookid="+this.bookid+"chapterid="+this.chapterId+"count="+this.count;
	}
	
	
}
