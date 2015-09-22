package com.me.lukens

import org.htmlcleaner._
import java.net.URL
import scala.xml.XML
import com.github.tototoshi.csv.CSVWriter

object LwliesRecommends extends App {

  implicit val writer = CSVWriter.open("/Users/Luke/Downloads/lwlies-recommends.txt")

  def processPage(page: Int)(implicit writer: CSVWriter) = {

    val cleaner = new HtmlCleaner()
    val root = cleaner.clean(new URL("http://www.littlewhitelies.co.uk/lwlies-recommends?page=" + page))

    val props = new CleanerProperties()
    val body = new SimpleXmlSerializer(props).getAsString(root.getElementsByName("body", true)(0))

    val xml = XML.loadString(body)

    (xml \\ "div") filter (node => (node \ "@class").text == "article") foreach { film =>
      val title = (film \ "h3").text.replaceAll("""\*$""", "")
      val link = (film \ "h3" \ "a" \ "@href").text
      val review = (film \ "p").text
      val ratings = (film \\ "li").zipWithIndex map { case (score, index) =>
        (score \ "@class").text.replaceAll(""".*(\d)$""", "$1")
      }
      if (ratings.contains("5")) {
        writer writeRow Seq(
          title,
          s"&ldquo;$review&rdquo;<br/><br/>" +
          (if (!ratings.isEmpty) {
            s"""
              |<b>Anticipation:</b> ${ratings(0)}<br/>
              |<b>Enjoyment:</b> ${ratings(1)}<br/>
              |<b>In Retrospect:</b> ${ratings(2)}<br/><br/>
            """.stripMargin.replaceAll("""\s+""", " ").replaceAll("""^\s*""", "")
          } else { "" }) + link
        )
      }
    }

  }

  writer writeRow Seq("Title", "Review")

  (1 to 83) foreach processPage

}
