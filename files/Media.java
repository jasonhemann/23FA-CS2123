import tester.Tester;

// a piece of media
interface IMedia {
  
  // is this media really old?
  boolean isReallyOld();
  
  // are captions available in this language?
  boolean isCaptionAvailable(String language);
  
  // a string showing the proper display of the media
  String format();
}

// represents a movie
class Movie implements IMedia {
  String title;
  int year;
  ILoString captionOptions; // available captions
  
  Movie(String title, int year, ILoString captionOptions) {
    this.title = title;
    this.year = year;
    this.captionOptions = captionOptions;
  }
  
  public boolean isReallyOld() {
    return false;
  }
  
  public boolean isCaptionAvailable(String language) {
    return false;
  }
  
  public String format() {
    return "";
  }
}

// represents a TV episode
class TVEpisode implements IMedia {
  String title;
  String showName;
  int seasonNumber;
  int episodeOfSeason;
  ILoString captionOptions; // available captions

  TVEpisode(String title, String showName, int seasonNumber, int episodeOfSeason,
      ILoString captionOptions) {
    this.title = title;
    this.showName = showName;
    this.seasonNumber = seasonNumber;
    this.episodeOfSeason = episodeOfSeason;
    this.captionOptions = captionOptions;
  }

  public boolean isReallyOld() {
    return false;
  }

  public boolean isCaptionAvailable(String language) {
    return false;
  }

  public String format() {
    return "";
  }
}

// represents a YouTube video
class YTVideo implements IMedia {
  String title;
  String channelName;
  ILoString captionOptions; // available captions
  
  public YTVideo(String title, String channelName, ILoString captionOptions) {
    this.title = title;
    this.channelName = channelName;
    this.captionOptions = captionOptions;
  }
  
  public boolean isReallyOld() {
    return false;
  }
  
  public boolean isCaptionAvailable(String language) {
    return false;
  }
  
  public String format() {
    return "";
  }
  
}

// lists of strings
interface ILoString {}

// an empty list of strings
class MtLoString implements ILoString {}

// a non-empty list of strings
class ConsLoString implements ILoString {
  String first;
  ILoString rest;
  
  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }
}

class ExamplesMedia {}
