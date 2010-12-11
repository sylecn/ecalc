/*
 * the virtual machine can work on its own without ecalc UI. But it remains in
 * ecalc.vm namespace.
 */
if (typeof ecalc === 'undefined') {
    var ecalc = {};
    alert('Warning: ecalc not defined.');
}

ecalc.vm = {
    /**
     * constructor for a VM.
     */
    VirtualMachine: function (name) {
	var _virtualKeys = [
	    // basic functions
	    'NUM0', 'NUM1', 'NUM2', 'NUM3', 'NUM4',
	    'NUM5', 'NUM6', 'NUM7', 'NUM8', 'NUM9', 'DOT',
	    'ADD', 'SUBTRACT', 'BACKSPACE', 'EQUAL', 'CLEAR'
	];

	var _clear = function () {
	    this.numberStack = [];
	    this.opStack = [];

	    this.acceptNumber = true;
	    this.partialNumber = '';
	};

	// trivial
	this.name = name;
	this.keyStack = [];

	// stacks
	this.numberStack = [];
	this.opStack = [];

	// state control
	this.acceptNumber = true;
	this.partialNumber = '';

	// represent this VM states in HTML
	this.asHTML = function () {
	    return '<div class="vm">Virtual Machine: ' + name +
		'<br/>keyStack: ' + this.keyStack.join(' ') + '</div>';
	}
	/**
	 * press key on virtual machine.
	 */
	this.pressKey = function (key) {
	    utils.assertTrue(utils.inList(key, _virtualKeys));
	    this.keyStack.push(key);

	    switch (key) {
	    case 'CLEAR':
		_clear();
	    }
	}
    }
};
