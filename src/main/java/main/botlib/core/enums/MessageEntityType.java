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

package main.botlib.core.enums;

/**
 * Types of the message entities.
 */
public enum MessageEntityType
{
	MENTION("mention"), /** @username */
	HASHTAG("hashtag"),
	BOT_COMMAND("bot_command"),
	URL("url"),
	EMAIL("email"),
	BOLD("bold"), /** bold text */
	ITALIC("italic"), /** italic text */
	CODE("code"), /** monowidth string */
	PRE("pre"), /** monowidth block */
	TEXT_LINK("text_link"), /** for clickable text URLs */
	TEXT_MENTION("text_mention"); /** for users without usernames */
	
	private final String string;
	
	MessageEntityType(String string)
	{
		this.string = string;
	}
	
	@Override
	public String toString()
	{
		return this.string;
	}
}