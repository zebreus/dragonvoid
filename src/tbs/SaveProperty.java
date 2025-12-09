package tbs;

public class SaveProperty<T> {
static final int STRING = 0;
static final int INT = 1;
static final int BOOLEAN = 2;
static final int FLOAT = 3;
/**
 * @uml.property  name="name"
 */
String name;
/**
 * @uml.property  name="content"
 */
T content;
public SaveProperty(String name,T content){
	this.name = name;
	this.content = content;
}
public void setContent(T content){
	this.content = content;
}
public T getContent(){
	return content;
}
}
