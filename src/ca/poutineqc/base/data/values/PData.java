package ca.poutineqc.base.data.values;

public abstract class PData {

	public static final PData INTEGER = new PData() {
		
		@Override
		public String toSString(Object value) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getSaveLength() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	
	public PData() {
		
	}
	
	public abstract String toSString(Object value);
	public abstract int getSaveLength();

}
