import java.awt.Color;

import tester.Tester;
import javalib.funworld.WorldScene;
import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.*;

class WellFormedHTML implements IDOMNodeVisitor<Boolean> {

    @Override
    public Boolean apply(IDOMNode arg) {
        return arg.accept(this);
    }

    @Override
    public Boolean visitBody(BodyElement body) {
        return body.contents.andMap(new OnlyBlocks());
    }

    @Override
    public Boolean visitDiv(DivElement div) { return false; }

    @Override
    public Boolean visitPara(ParaElement para) { return false; }

    @Override
    public Boolean visitHeader(HeaderElement header) { return false; }

    @Override
    public Boolean visitText(Text text) { return false; }

    @Override
    public Boolean visitAnchor(AnchorElement anchor) { return false; }

    @Override
    public Boolean visitBold(BoldElement bold) { return false; }

    @Override
    public Boolean visitItalic(ItalicElement italic) { return false; }
}

class OnlyBlocks implements IDOMNodeVisitor<Boolean> {
    public Boolean apply(IDOMNode arg) {
        return arg.accept(this);
    }

    @Override
    public Boolean visitBody(BodyElement body) { return false; }

    @Override
    public Boolean visitDiv(DivElement div) {
        return div.contents.andMap(this);
    }

    @Override
    public Boolean visitPara(ParaElement para) {
        return para.contents.andMap(new OnlyInline());
    }

    @Override
    public Boolean visitHeader(HeaderElement header) {
        return header.contents.andMap(new OnlyInline());
    }

    @Override
    public Boolean visitText(Text text) { return false; }

    @Override
    public Boolean visitAnchor(AnchorElement anchor) { return false; }

    @Override
    public Boolean visitBold(BoldElement bold) { return false; }

    @Override
    public Boolean visitItalic(ItalicElement italic) { return false; }
}

class OnlyInline implements IDOMNodeVisitor<Boolean> {
    public Boolean apply(IDOMNode arg) {
        return arg.accept(this);
    }

    @Override
    public Boolean visitBody(BodyElement body) { return false; }

    @Override
    public Boolean visitDiv(DivElement div) { return false; }

    @Override
    public Boolean visitPara(ParaElement para) { return false; }

    @Override
    public Boolean visitHeader(HeaderElement header) { return false; }

    @Override
    public Boolean visitText(Text text) {
        return true;
    }

    @Override
    public Boolean visitAnchor(AnchorElement anchor) {
        return anchor.contents.andMap(this);
    }

    @Override
    public Boolean visitBold(BoldElement bold) {
        return bold.contents.andMap(this);
    }

    @Override
    public Boolean visitItalic(ItalicElement italic) {
        return italic.contents.andMap(this);
    }
}

//A simple helper function to compose two images beside each other
class BesideImages implements IFunc2<WorldImage, WorldImage, WorldImage> {
 public WorldImage apply(WorldImage left, WorldImage right) {
     return new BesideImage(left, right);
 }
}

//A simple helper function to compose two images one below the other, aligned left
class BelowImages implements IFunc2<WorldImage, WorldImage, WorldImage> {
 public WorldImage apply(WorldImage top, WorldImage bot) {
     return new AboveAlignImage(AlignModeX.LEFT, top, bot);
     //return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.PINHOLE, top, 0, 
     //    top.getHeight() / 2 + bot.getHeight() / 2, bot);
 }
}

