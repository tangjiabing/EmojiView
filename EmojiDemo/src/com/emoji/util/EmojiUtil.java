package com.emoji.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

/**
 * 
 * @author tangjiabing
 * 
 * @see 开源时间：2016年03月31日
 * 
 *      记得给我个star哦~
 * 
 */
public class EmojiUtil {

	public static final String ee_static_000 = "[:D]";
	public static final String ee_static_001 = "[;)]";
	public static final String ee_static_002 = "[(|)]";
	public static final String ee_static_003 = "[(u)]";
	public static final String ee_static_004 = "[(S)]";
	public static final String ee_static_005 = "[(*)]";
	public static final String ee_static_006 = "[(#)]";
	public static final String ee_static_007 = "[(R)]";
	public static final String ee_static_008 = "[+o(]";
	public static final String ee_static_009 = "[:'(]";
	public static final String ee_static_010 = "[(k)]";
	public static final String ee_static_011 = "[:-#]";
	public static final String ee_static_012 = "[8-|]";
	public static final String ee_static_013 = "[(F)]";
	public static final String ee_static_014 = "[(W)]";
	public static final String ee_static_015 = "[(D)]";
	public static final String ee_static_016 = "[(K)]";
	public static final String ee_static_017 = "[:(]";
	public static final String ee_static_018 = "[:>]";
	public static final String ee_static_019 = "[({)]";
	public static final String ee_static_020 = "[:##]";
	public static final String ee_static_021 = "[(H)]";
	public static final String ee_static_022 = "[:s]";
	public static final String ee_static_023 = "[):]";
	public static final String ee_static_024 = "[:@]";
	public static final String ee_static_025 = "[:|]";
	public static final String ee_static_026 = "[8'<]";
	public static final String ee_static_027 = "[:'i]";
	public static final String ee_static_028 = "[8i]";
	public static final String ee_static_029 = "[:p]";
	public static final String ee_static_030 = "[::i]";
	public static final String ee_static_031 = "[:i:]";
	public static final String ee_static_032 = "[-})]";
	public static final String ee_static_033 = "[:-o]";
	public static final String ee_static_034 = "[^?-o]";
	public static final String ee_static_035 = "[s|-o]";
	public static final String ee_static_036 = "[(})]";
	public static final String ee_static_037 = "[*)h]";
	public static final String ee_static_038 = "[^|m*]";
	public static final String ee_static_039 = "[s^|%]";
	public static final String ee_static_040 = "[*-)]";
	public static final String ee_static_041 = "[^o)]";
	public static final String ee_static_042 = "[^o*)]";
	public static final String ee_static_043 = "[8-)]";
	public static final String ee_static_044 = "[^o})]";
	public static final String ee_static_045 = "[|-)]";
	public static final String ee_static_046 = "[8o|]";
	public static final String ee_static_047 = "[{o|]";
	public static final String ee_static_048 = "[{|o*}]";
	public static final String ee_static_049 = "[{o|*o}]";
	public static final String ee_static_050 = "[^n|*o}]";
	public static final String ee_static_051 = "[^#|*}]";
	public static final String ee_static_052 = "[*#*k}]";
	public static final String ee_static_053 = "[(#3*}]";
	public static final String ee_static_054 = "[4d*)]";
	public static final String ee_static_055 = "[o@d*)]";
	public static final String ee_static_056 = "[o6i*|]";
	public static final String ee_static_057 = "[1o^i*(]";
	public static final String ee_static_058 = "[2g^j)}]";
	public static final String ee_static_059 = "[ko*h)}]";

	private static final Factory spannableFactory = Spannable.Factory
			.getInstance();
	private static final Map<Pattern, Integer> emojiMap = new HashMap<Pattern, Integer>();

	private static ResUtil resUtil = null;
	private static boolean isLoadIcon = false;

	// ***************************************************************************************
	// 公有方法

	public static Spannable getEmojiText(Context context, CharSequence text) {
		if (isLoadIcon == false) {
			isLoadIcon = true;
			resUtil = new ResUtil(context);
			loadIcon();
		}
		Spannable spannable = spannableFactory.newSpannable(text);
		addEmoji(context, spannable);
		return spannable;
	}

	public static boolean containsKey(String key) {
		boolean b = false;
		for (Entry<Pattern, Integer> entry : emojiMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(key);
			if (matcher.find()) {
				b = true;
				break;
			}
		}
		return b;
	}

	// ***************************************************************************************
	// 私有方法

	private static void loadIcon() {
		addPattern(emojiMap, ee_static_000,
				resUtil.getIdFromDrawable("ee_static_000"));
		addPattern(emojiMap, ee_static_001,
				resUtil.getIdFromDrawable("ee_static_001"));
		addPattern(emojiMap, ee_static_002,
				resUtil.getIdFromDrawable("ee_static_002"));
		addPattern(emojiMap, ee_static_003,
				resUtil.getIdFromDrawable("ee_static_003"));
		addPattern(emojiMap, ee_static_004,
				resUtil.getIdFromDrawable("ee_static_004"));
		addPattern(emojiMap, ee_static_005,
				resUtil.getIdFromDrawable("ee_static_005"));
		addPattern(emojiMap, ee_static_006,
				resUtil.getIdFromDrawable("ee_static_006"));
		addPattern(emojiMap, ee_static_007,
				resUtil.getIdFromDrawable("ee_static_007"));
		addPattern(emojiMap, ee_static_008,
				resUtil.getIdFromDrawable("ee_static_008"));
		addPattern(emojiMap, ee_static_009,
				resUtil.getIdFromDrawable("ee_static_009"));
		addPattern(emojiMap, ee_static_010,
				resUtil.getIdFromDrawable("ee_static_010"));
		addPattern(emojiMap, ee_static_011,
				resUtil.getIdFromDrawable("ee_static_011"));
		addPattern(emojiMap, ee_static_012,
				resUtil.getIdFromDrawable("ee_static_012"));
		addPattern(emojiMap, ee_static_013,
				resUtil.getIdFromDrawable("ee_static_013"));
		addPattern(emojiMap, ee_static_014,
				resUtil.getIdFromDrawable("ee_static_014"));
		addPattern(emojiMap, ee_static_015,
				resUtil.getIdFromDrawable("ee_static_015"));
		addPattern(emojiMap, ee_static_016,
				resUtil.getIdFromDrawable("ee_static_016"));
		addPattern(emojiMap, ee_static_017,
				resUtil.getIdFromDrawable("ee_static_017"));
		addPattern(emojiMap, ee_static_018,
				resUtil.getIdFromDrawable("ee_static_018"));
		addPattern(emojiMap, ee_static_019,
				resUtil.getIdFromDrawable("ee_static_019"));
		addPattern(emojiMap, ee_static_020,
				resUtil.getIdFromDrawable("ee_static_020"));
		addPattern(emojiMap, ee_static_021,
				resUtil.getIdFromDrawable("ee_static_021"));
		addPattern(emojiMap, ee_static_022,
				resUtil.getIdFromDrawable("ee_static_022"));
		addPattern(emojiMap, ee_static_023,
				resUtil.getIdFromDrawable("ee_static_023"));
		addPattern(emojiMap, ee_static_024,
				resUtil.getIdFromDrawable("ee_static_024"));
		addPattern(emojiMap, ee_static_025,
				resUtil.getIdFromDrawable("ee_static_025"));
		addPattern(emojiMap, ee_static_026,
				resUtil.getIdFromDrawable("ee_static_026"));
		addPattern(emojiMap, ee_static_027,
				resUtil.getIdFromDrawable("ee_static_027"));
		addPattern(emojiMap, ee_static_028,
				resUtil.getIdFromDrawable("ee_static_028"));
		addPattern(emojiMap, ee_static_029,
				resUtil.getIdFromDrawable("ee_static_029"));
		addPattern(emojiMap, ee_static_030,
				resUtil.getIdFromDrawable("ee_static_030"));
		addPattern(emojiMap, ee_static_031,
				resUtil.getIdFromDrawable("ee_static_031"));
		addPattern(emojiMap, ee_static_032,
				resUtil.getIdFromDrawable("ee_static_032"));
		addPattern(emojiMap, ee_static_033,
				resUtil.getIdFromDrawable("ee_static_033"));
		addPattern(emojiMap, ee_static_034,
				resUtil.getIdFromDrawable("ee_static_034"));
		addPattern(emojiMap, ee_static_035,
				resUtil.getIdFromDrawable("ee_static_035"));
		addPattern(emojiMap, ee_static_036,
				resUtil.getIdFromDrawable("ee_static_036"));
		addPattern(emojiMap, ee_static_037,
				resUtil.getIdFromDrawable("ee_static_037"));
		addPattern(emojiMap, ee_static_038,
				resUtil.getIdFromDrawable("ee_static_038"));
		addPattern(emojiMap, ee_static_039,
				resUtil.getIdFromDrawable("ee_static_039"));
		addPattern(emojiMap, ee_static_040,
				resUtil.getIdFromDrawable("ee_static_040"));
		addPattern(emojiMap, ee_static_041,
				resUtil.getIdFromDrawable("ee_static_041"));
		addPattern(emojiMap, ee_static_042,
				resUtil.getIdFromDrawable("ee_static_042"));
		addPattern(emojiMap, ee_static_043,
				resUtil.getIdFromDrawable("ee_static_043"));
		addPattern(emojiMap, ee_static_044,
				resUtil.getIdFromDrawable("ee_static_044"));
		addPattern(emojiMap, ee_static_045,
				resUtil.getIdFromDrawable("ee_static_045"));
		addPattern(emojiMap, ee_static_046,
				resUtil.getIdFromDrawable("ee_static_046"));
		addPattern(emojiMap, ee_static_047,
				resUtil.getIdFromDrawable("ee_static_047"));
		addPattern(emojiMap, ee_static_048,
				resUtil.getIdFromDrawable("ee_static_048"));
		addPattern(emojiMap, ee_static_049,
				resUtil.getIdFromDrawable("ee_static_049"));
		addPattern(emojiMap, ee_static_050,
				resUtil.getIdFromDrawable("ee_static_050"));
		addPattern(emojiMap, ee_static_051,
				resUtil.getIdFromDrawable("ee_static_051"));
		addPattern(emojiMap, ee_static_052,
				resUtil.getIdFromDrawable("ee_static_052"));
		addPattern(emojiMap, ee_static_053,
				resUtil.getIdFromDrawable("ee_static_053"));
		addPattern(emojiMap, ee_static_054,
				resUtil.getIdFromDrawable("ee_static_054"));
		addPattern(emojiMap, ee_static_055,
				resUtil.getIdFromDrawable("ee_static_055"));
		addPattern(emojiMap, ee_static_056,
				resUtil.getIdFromDrawable("ee_static_056"));
		addPattern(emojiMap, ee_static_057,
				resUtil.getIdFromDrawable("ee_static_057"));
		addPattern(emojiMap, ee_static_058,
				resUtil.getIdFromDrawable("ee_static_058"));
		addPattern(emojiMap, ee_static_059,
				resUtil.getIdFromDrawable("ee_static_059"));
	}

	private static void addPattern(Map<Pattern, Integer> map, String emoji,
			int resource) {
		map.put(Pattern.compile(Pattern.quote(emoji)), resource);
	}

	private static boolean addEmoji(Context context, Spannable spannable) {
		boolean isAddSuccess = false;
		for (Entry<Pattern, Integer> entry : emojiMap.entrySet()) {
			Matcher matcher = entry.getKey().matcher(spannable);
			while (matcher.find()) {
				spannable.setSpan(new ImageSpan(context, entry.getValue()),
						matcher.start(), matcher.end(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 只能用SPAN_EXCLUSIVE_EXCLUSIVE，具体原因不知
				isAddSuccess = true;
			}
		}
		return isAddSuccess;
	}
}
