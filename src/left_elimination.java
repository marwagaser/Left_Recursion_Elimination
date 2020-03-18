import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class left_elimination {
	public static String[] splitString(String s, String operator) {
		String[] sections = s.split(operator);
		return sections;
	}

	public static void substitution(Map<String, ArrayList<String>> map, String keySub, String key,
			HashMap<String, ArrayList<String>> hmap) {
		// check if LHS of values in key = to keySub. If so, replace them with the
		// values of the keySub
		ArrayList<String> al = map.get(key);
		ArrayList<String> unchanged = new ArrayList<String>();
		ArrayList<String> toBeChanged = new ArrayList<String>();
		for (int i = 0; i < al.size(); i++) {
			if ((al.get(i).charAt(0) + "").equals(keySub)) {
				// replace it with the values of the keySub
				toBeChanged.add(al.get(i).substring(1));
			} else {
				unchanged.add(al.get(i));
			}
		}
		if (toBeChanged.size() < 1) {
			return;
		} else {
			ArrayList<String> finalAL = new ArrayList<String>();
			for (int k = 0; k < toBeChanged.size(); k++) {
				int size = map.get(keySub).size();
				for (int i = 0; i < size; i++) {
					String x = toBeChanged.get(k);
					x = map.get(keySub).get(i) + x;
					finalAL.add(x);
				}
			}
			// merge bothFinalAL and unchanged
			Set<String> set = new LinkedHashSet<>(unchanged);
			set.addAll(finalAL);
			ArrayList<String> combinedList = new ArrayList<>(set);
			map.replace(key, combinedList);
		}
	}

	public static boolean noLeftRecursion(ArrayList<String> arrayList, String key) {
		for (int i = 0; i < arrayList.size(); i++) {
			if ((arrayList.get(i).charAt(0) + "").equals(key)) {
				return false;
			}
		}
		return true;
	}

	public static void leftElimination(Map<String, ArrayList<String>> map, String key,
			HashMap<String, ArrayList<String>> hmap) {
		ArrayList<String> arrayList = map.get(key);

		// check if the starting letter == key
		ArrayList<String> arrayOfAlphas = new ArrayList<String>();
		ArrayList<String> arrayOfBetas = new ArrayList<String>();
		boolean flag = noLeftRecursion(arrayList, key);
		if (flag == false) {

			for (int i = 0; i < arrayList.size(); i++) {

				if ((arrayList.get(i).charAt(0) + "").equals(key)) {
					// create dash and fix the sequence
					arrayOfAlphas.add(arrayList.get(i).substring(1) + key + "'");
				} else {
					arrayOfBetas.add(arrayList.get(i) + key + "'");
				}
			}
			// create two new rules for each case
			map.replace(key, arrayOfBetas);
			hmap.put(key + "'", arrayOfAlphas);
		}

	}

	public static String LRE(String str) {
		Map<String, ArrayList<String>> map = new LinkedHashMap<String, ArrayList<String>>();
		HashMap<String, ArrayList<String>> hmap = new LinkedHashMap<String, ArrayList<String>>();
		// str.matches("-?(0|[1-9]\\d*)");
		String[] CFG_vars = splitString(str, ";");
		for (int i = 0; i < CFG_vars.length; i++) {
			ArrayList<String> arrList = new ArrayList<String>();
			String[] singleVar = splitString(CFG_vars[i], ",");
			String name = singleVar[0];
			for (int j = 1; j < singleVar.length; j++) {
				arrList.add(singleVar[j]);
			}
			map.put(name, arrList);
		}
		ArrayList<String> keys = new ArrayList<String>(map.keySet());
		for (int i = 0; i < keys.size(); i++) {
			for (int j = 0; j < i; j++) {

				substitution(map, keys.get(j), keys.get(i), hmap);
			}
			leftElimination(map, keys.get(i), hmap);
		}
		String answer = "";
		Set<String> keysx = map.keySet();
		for (String key1 : keysx) {
			answer += key1 + "," + map.get(key1).toString().replace("[", "").replace("]", "") + ";";
		}
		Set<String> keysx1 = hmap.keySet();
		for (String key1 : keysx1) {
			answer += key1 + "," + hmap.get(key1).toString().replace("[", "").replace("]", "") + ",;";
		}
		answer = answer.substring(0, answer.length() - 1);
		answer = answer.replaceAll("\\s","");
		return answer;

	}

	public static void main(String[] args) {
		String input = "S,ScT,T;T,aSb,iaLb,i;L,SdL,S";
		String output = LRE(input);
		System.out.println(output);

		String input2 = "S,Sa,b";
		String output2 = LRE(input2);
		System.out.println(output2);

		String input3 = "S,Sab,cd";
		String output3 = LRE(input3);
		System.out.println(output3);

		String input4 = "S,SuS,SS,Ss,lSr,a";
		String output4 = LRE(input4);
		System.out.println(output4);

		String input5 = "S,SuT,T;T,TF,F;F,Fs,P;P,a,b";
		String output5 = LRE(input5);
		System.out.println(output5);

		String input6 = "S,z,To;T,o,Sz";
		String output6 = LRE(input6);
		System.out.println(output6);

		String input7 = "S,lLr,a;L,LbS,S";
		String output7 = LRE(input7);
		System.out.println(output7);


		String input9 = "S,BC,C;B,Bb,b;C,SC,a";
		String output9 = LRE(input9);
		System.out.println(output9);

	}
}
