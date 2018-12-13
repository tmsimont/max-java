autowatch = 1;

/**
 * Helper to make something in the parent patcher of a bpatcher.
 * Java doesn't have `this.patcher.parentpatcher` so this helps the Java layer
 * do what only js can do...
 * Call to "make bang" for example makes a bang in the parent of this bpatcher.
 * All args after "bang" (the type) will be passed to the newdefault() function.
 */
function make(type) {
	var p = this.patcher.parentpatcher;
	var a = arrayfromargs(messagename, arguments);
	a.shift(); // drop "make" from args
	p.newdefault.apply(p, a);
}

/**
 * Again Java world's hands are tied in a bpatcher context. This reaches up to the parent
 *  of a bpatcher and grabs a named max object, and sends the given message (with any additional args).
 */
function message_named_child_of_bpatcher(bpatcher_name, child_name, message) {
    var target = this.patcher.parentpatcher.getnamed(bpatcher_name);
	var a = arrayfromargs(messagename, arguments);
	a.shift(); // drop fname from args
	a.shift(); // drop bpatcher_name from args
	a.shift(); // drop child_name from args
    var child = target.subpatcher().getnamed(child_name);
    child.message.apply(child, a);
}

function resize_bpatcher(bpatcher_name, width, height){
    var target = this.patcher.parentpatcher.getnamed(bpatcher_name);
    var x = target.rect[0];
    var y = target.rect[1];
    target.rect = [x, y, x + width, y + height];
}
