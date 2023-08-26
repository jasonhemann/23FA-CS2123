/** Our standard generic lists */
interface IList<T> {
    <U> IList<U> map(IFunc<T, U> mapFunc);

    <U> U foldr(IFunc2<T, U, U> foldFunc, U base);

    <U> U foldl(IFunc2<T, U, U> foldFunc, U base);

    boolean andMap(IFunc<T, Boolean> pred);
}

class Empty<T> implements IList<T> {
    public <U> IList<U> map(IFunc<T, U> mapFunc) {
        return new Empty<U>();
    }

    public <U> U foldr(IFunc2<T, U, U> foldFunc, U base) {
        return base;
    }

    public <U> U foldl(IFunc2<T, U, U> foldFunc, U base) {
        return base;
    }
    
    public boolean andMap(IFunc<T, Boolean> pred) {
        return true;
    }
}

class Cons<T> implements IList<T> {
    T first;
    IList<T> rest;

    Cons(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }

    public <U> IList<U> map(IFunc<T, U> mapFunc) {
        return new Cons<U>(mapFunc.apply(this.first), this.rest.map(mapFunc));
    }

    public <U> U foldr(IFunc2<T, U, U> foldFunc, U base) {
        return foldFunc.apply(this.first, this.rest.foldr(foldFunc, base));
    }

    public <U> U foldl(IFunc2<T, U, U> foldFunc, U base) {
        return this.rest.foldl(foldFunc, foldFunc.apply(this.first, base));
    }
    
    public boolean andMap(IFunc<T, Boolean> pred) {
        return pred.apply(this.first) && this.rest.andMap(pred);
    }
}

/**
 *  Functions of one argument
 * 
 * @param <A>  The argument type
 * @param <R>  The return type
 */
interface IFunc<A, R> {
    R apply(A arg);
}

/**
 *  Functions of two arguments
 * 
 * @param <A1>  The first argument type
 * @param <A2>  The second argument type
 * @param <R>   The return type
 */
interface IFunc2<A1, A2, R> {
    R apply(A1 arg1, A2 arg2);
}






/** 
 * A simple helper function-object that concatenates two strings, 
 * with the given separator string in between 
 */
class ConcatStrings implements IFunc2<String, String, String> {
    String separator;
    ConcatStrings() {
        this("");
    }
    ConcatStrings(String separator) {
        this.separator = separator;
    }
    public String apply(String first, String second) {
        return first + this.separator + second;
    }
}






/**
 * The general interface of all DOM nodes
 *
 */
interface IDOMNode {
    <R> R accept(IDOMNodeVisitor<R> visitor);
}

/**
 *  The Body element contains all the other content of the page
 *  <p>It should only contain Block Elements</p>
 */
class BodyElement implements IDOMNode {
    IList<IDOMNode> contents;
    BodyElement(IList<IDOMNode> contents) {
        this.contents = contents;
    }
    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitBody(this);
    }
}

/**
 *  Block Elements are things like headers and paragraphs,
 *  which are vertically separated from each other.
 *  <p>Block Elements should only contain Inline Elements</p>
 */
abstract class BlockElement implements IDOMNode {
    IList<IDOMNode> contents;

    BlockElement(IList<IDOMNode> contents) {
        this.contents = contents;
    }
}

/**
 *  Inline Elements are things like runs of text, links, bold or italic items, etc,
 *  which are strung together without line breaks.
 *  <p>Inline Elements might contain other Inline Elements, 
 *  but should not contain Block Elements</p>
 */
abstract class InlineElement implements IDOMNode {

}

/**
 *  DOM Visitors: implement functions over arbitrary DOM content
 * 
 *  @param <R>  The result type of the function
 */
interface IDOMNodeVisitor<R> extends IFunc<IDOMNode, R> {
    // The root: the body of the document
    R visitBody(BodyElement body);
    // Block elements
    R visitDiv(DivElement div);
    
    R visitPara(ParaElement para);

    R visitHeader(HeaderElement header);
    // Inline elements
    R visitText(Text text);

    R visitAnchor(AnchorElement anchor);

    R visitBold(BoldElement bold);

    R visitItalic(ItalicElement italic);
}

////// Concrete classes for DOM elements //////////

/**
 *  Div elements represent divisions of the document
 *  <p>They should contain only Block Elements</p>
 */
class DivElement extends BlockElement {
    DivElement(IList<IDOMNode> contents) {
        super(contents);
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitDiv(this);
    }
}


/**
 *  Para elements represent paragraphs of content
 *  <p>They should contain only Inline Elements</p>
 */
class ParaElement extends BlockElement {
    ParaElement(IList<IDOMNode> contents) {
        super(contents);
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitPara(this);
    }
}

/**
 *  Header elements represent headers of various levels (like titles, subtitles, sections, 
 *  subsections, etc.)  
 *  <p>Headers should contain only Inline Elements.</p>
 */
class HeaderElement extends BlockElement {
    int level;

    HeaderElement(int level, IList<IDOMNode> contents) {
        super(contents);
        this.level = level;
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitHeader(this);
    }
}

/**
 *  Text is not really an element in the traditional sense, but is the base case for our tree:
 *  it represents a simple string of unformatted text
 */
class Text extends InlineElement {
    String contents;

    Text(String contents) {
        this.contents = contents;
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitText(this);
    }
}

/**
 *  Anchor elements represent hyperlinks.
 *  <p>They should only contain Inline Elements.</p>
 */
class AnchorElement extends InlineElement {
    String target;
    IList<IDOMNode> contents;

    AnchorElement(String target, IList<IDOMNode> contents) {
        this.target = target;
        this.contents = contents;
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitAnchor(this);
    }
}

/**
 *  Bold elements format their content as <b>bold</b> text.
 *  <p>That content should only be Inline Elements.</p>
 */
class BoldElement extends InlineElement {
    IList<IDOMNode> contents;

    BoldElement(IList<IDOMNode> contents) {
        this.contents = contents;
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitBold(this);
    }
}

/**
 *  Italic elements format their content as <i>italic</i> text.
 *  <p>That content should only be Inline Elements.</p>
 */
class ItalicElement extends InlineElement {
    IList<IDOMNode> contents;

    ItalicElement(IList<IDOMNode> contents) {
        this.contents = contents;
    }

    public <R> R accept(IDOMNodeVisitor<R> visitor) {
        return visitor.visitItalic(this);
    }
}
