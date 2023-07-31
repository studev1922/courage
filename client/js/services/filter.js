// arr:Array, values:Array, key:String
app.filter('has', () => util.has);
// arr:Array, column:String, desc:Boolean
app.filter('sort', () => util.sort);
// arr:Array
app.filter('total', () => util.total)
// arr:Array, column:String, range: {min:Number, max:Number}
app.filter('between', () => util.between)