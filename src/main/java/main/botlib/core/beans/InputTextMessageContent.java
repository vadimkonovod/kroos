/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Fouad Almalki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package main.botlib.core.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.botlib.core.enums.ParseMode;

/**
 * Represents the content of a text message to be sent as the result
 * of an inline query.
 */
public class InputTextMessageContent extends InputMessageContent
{
	/**
	 * Text of the message to be sent, 1-4096 characters.
	 */
	@JsonProperty("message_text")
	private String messageText;
	
	/**
	 * Optional. send Markdown or HTML, if you want Telegram apps to show
	 * bold, italic, fixed-width text or inline URLs in your bot's message.
	 */
	@JsonProperty("parse_mode")
	private ParseMode parseMode;
	
	/**
	 * Optional. disables link previews for links in the sent message.
	 */
	@JsonProperty("disable_web_page_preview")
	private Boolean disableLinkPreviews;
	
	public InputTextMessageContent(){}
	
	public InputTextMessageContent(String messageText, ParseMode parseMode, Boolean disableLinkPreviews)
	{
		this.messageText = messageText;
		this.parseMode = parseMode;
		this.disableLinkPreviews = disableLinkPreviews;
	}
	
	public String getMessageText(){return messageText;}
	public void setMessageText(String messageText){this.messageText = messageText;}
	
	public ParseMode getParseMode(){return parseMode;}
	public void setParseMode(ParseMode parseMode){this.parseMode = parseMode;}
	
	public Boolean getDisableLinkPreviews(){return disableLinkPreviews;}
	public void setDisableLinkPreviews(Boolean disableLinkPreviews){this.disableLinkPreviews = disableLinkPreviews;}
	
	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(!(o instanceof InputTextMessageContent)) return false;
		
		InputTextMessageContent that = (InputTextMessageContent) o;
		
		if(messageText != null ? !messageText.equals(that.messageText) : that.messageText != null) return false;
		if(parseMode != that.parseMode) return false;
		return disableLinkPreviews != null ? disableLinkPreviews.equals(that.disableLinkPreviews)
		                                   : that.disableLinkPreviews == null;
		
	}
	
	@Override
	public int hashCode()
	{
		int result = messageText != null ? messageText.hashCode() : 0;
		result = 31 * result + (parseMode != null ? parseMode.hashCode() : 0);
		result = 31 * result + (disableLinkPreviews != null ? disableLinkPreviews.hashCode() : 0);
		return result;
	}
	
	@Override
	public String toString()
	{
		return "InputTextMessageContent{" +
				"messageText='" + messageText + '\'' +
				", parseMode=" + parseMode +
				", disableLinkPreviews=" + disableLinkPreviews +
				"}";
	}
}