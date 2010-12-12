/*
 * the virtual machine can work on its own without ecalc UI. But it remains in
 * ecalc.vm namespace.
 */
if (typeof ecalc === 'undefined') {
    var ecalc = {};
    alert('Warning: ecalc not defined.');
}

ecalc.vm = {
    log: function (msg) {
	ecalc.log(msg);
    },
    /**
     * constructor for a History
     */
    History: function () {
	// pretty print Operators
	var printOp = function (op) {
	    switch (op) {
	    case 'ADD': return '+';
	    case 'SUBTRACT': return '-';
	    default: return '';
	    }
	};
	// pretty print result, keep 2 digits after point
	var printResult = function (number) {
	    var format = /[^.]*(\.\d{0,2})?/;
	    return format.exec(number + '')[0];
	};
	this._all = [];
	this._numberStack = [];
	this._resultStack = [];
	this._opStack = [];

	this.pushNumber = function (number) {
	    this._numberStack.push(number);
	};
	this.pushResult = function (number) {
	    this._resultStack.push(number);
	};
	this.pushOp = function (op) {
	    this._opStack.push(op);
	};
	this.calcDone = function () {
	    // keep only latest 10
	    if (this._all.length > 10) {
		utils.assert(false, 'history._all should never have more than\
10 members');
	    }
	    if (this._all.length == 10) {
		this._all.shift();
	    }
	    this._all.push({
		nS: this._numberStack,
		rS: this._resultStack,
		oS: this._opStack
	    });
	    this._numberStack = [];
	    this._resultStack = [];
	    this._opStack = [];
	}
	this._oneCalcAsHTML = function(nS, oS, rS) {
	    var table = '<table class="history">';
	    for (var i = 0; i < nS.length; i++) {
		table += '<tr><td class="left">' + nS[i] + '</td>' +
		    '<td class="right result">' + printResult(rS[i - 1] || '') +
		    '</td></tr><tr><td class="left">' + printOp(oS[i] || '') +
		    '</td><td class="right"></td></tr>';
	    };
	    table += '</table>';
	    return table;
	};
	// show most recent histories.
	this.asHTML = function () {
	    var re = '<div>History<table id="all-history"><tr>';
	    var nS, oS, rS;
	    // show howMany sessions? default is 3.
	    var howMany = 3;
	    var len;
	    if (this._numberStack.length) {
		// easier to type
		nS = this._numberStack;
		oS = this._opStack;
		rS = this._resultStack;
		re += '<td class="top">' +
		    this._oneCalcAsHTML(nS, oS, rS) + '</td>';
		howMany -= 1;
	    }
	    len = this._all.length;
	    if (len < howMany) {
		howMany = len;
	    }
	    for (var i = 0; i < howMany; ++i) {
		nS = this._all[len - i - 1].nS;
		oS = this._all[len - i - 1].oS;
		rS = this._all[len - i - 1].rS;
		re += '<td class="top">' +
		    this._oneCalcAsHTML(nS, oS, rS) + '</td>';
	    }
	    return  re + '</tr></table></div>';
	};
    },
    /**
     * constructor for a VirtualMachine.
     */
    VirtualMachine: function (name) {
	var _virtualKeys = [
	    // ================
	    //  basic features
	    // ================
	    // digits
	    'NUM0', 'NUM1', 'NUM2', 'NUM3', 'NUM4',
	    'NUM5', 'NUM6', 'NUM7', 'NUM8', 'NUM9',
	    'DOT', 'NUM00',
	    // op
	    'ADD', 'SUBTRACT',
	    // misc
	    'BACKSPACE', 'EQUAL', 'CLEAR'
	    // ===================
	    //  advanced features
	    // ===================
	];

	/**
	 * reset partial number states
	 */
	this._resetPartialNumber = function () {
	    this._acceptDot = true;
	    this._partialNumber = '';
	}

	/**
	 * accept partial number.
	 */
	this._acceptPartialNumber = function () {
	    var number = parseFloat(this._partialNumber) || 0;
	    this._numberStack.push(number);
	    this._resetPartialNumber();

	    this.history.pushNumber(number);
	};

	/**
	 * CLEAR key pressed
	 */
	this._clear = function () {
	    // key stack
	    this._keyStack = [];
	    // stacks
	    this._numberStack = [];
	    this._opStack = [];

	    this._resetPartialNumber();
	    this._calcDone = false;
	    this._partialCalcDone = false;
	};

	/**
	 * if opStack is not empty. do calculation.
	 */
	this._doCalculationMaybe = function () {
	    var value1, value2, result;

	    op = this._opStack.pop();
	    if (op) {
		// calculate for last OP in stack
		value2 = this._numberStack.pop();
		value1 = this._numberStack.pop();
		switch (op) {
		case 'ADD':
		    result = value1 + value2;
		    break;
		case 'SUBTRACT':
		    result = value1 - value2;
		    break;
		default:
		    utils.assert(false, '_doCalculationMaybe(): bad op: ' + op);
		}
		this._numberStack.push(result);
		this._partialCalcDone = true;
		this.history.pushResult(result);
	    }
	};

	/**
	 * screen obejct
	 */
	// Don't know how to know the outter 'this' in inner object.
	// the following doesn't work.
	// this.screen = function () {
	//     var that = this;

	//     return {
	// 	asText: function () {
	// 	    return that._partialNumber || '0';
	// 	}
	//     };
	// } ();

	this.screen_asText = function () {
	    if (this._calcDone || this._partialCalcDone) {
		return this._numberStack.last();
	    } else {
		return this._partialNumber || '0';
	    }
	};

	// trivial
	this.name = name;
	this._keyStack = [];

	// stacks
	this._numberStack = [];
	this._opStack = [];

	// state control
	this._calcDone = false;
	this._acceptDot = true;
	this._partialNumber = '';

	// history
	this.history = new ecalc.vm.History();

	// ==================
	//  public functions
	// ==================

	// represent this VM states in HTML
	this.asHTML = function () {
	    return '<div class="vm">' + name +
		// show last 10 Key
		'<br/>keyStack: ' + this._keyStack.slice(-10).join(' ') +
		'<br/>_numberStack: ' + this._numberStack.join(' ') +
		'<br/>_opStack: ' + this._opStack.join(' ') +
		'<br/>_partialNumber: ' + this._partialNumber +
		'<br/>_calcDone: ' + this._calcDone +
		'</div>';
	}
	/**
	 * press key on virtual machine.
	 */
	this.pressKey = function (key) {
	    var value1, value2, op;
	    utils.assertTrue(utils.inList(key, _virtualKeys),
			    'pressKey(): bad key: ' + key);
	    this._keyStack.push(key);

	    switch (key) {
	    case 'CLEAR':
		this._clear();
		break;
	    case 'NUM0':  //all all the way through
	    case 'NUM00':
	    case 'NUM1':
	    case 'NUM2':
	    case 'NUM3':
	    case 'NUM4':
	    case 'NUM5':
	    case 'NUM6':
	    case 'NUM7':
	    case 'NUM8':
	    case 'NUM9':
		if (key == 'NUM0' || key == 'NUM00') {
		    if (this._partialNumber) {
			this._partialNumber += key.slice(3);
		    } else {
			ecalc.vm.log('ignore none significant ' + key);
		    }
		} else {
		    this._partialNumber += key.slice(3);
		}

		this._partialCalcDone = false;
		if (this._calcDone) {
		    // throw away non-reachable data in stack
		    this._numberStack = [];
		    this._opStack = [];
		    this._calcDone = false;
		}
		break;
	    case 'DOT':
		if (this._acceptDot) {
		    if (this._partialNumber) {
			this._partialNumber += '.';
		    } else {
			this._partialNumber = '0.';
		    }
		    this._acceptDot = false;
		} else {
		    ecalc.vm.log('Warning: ignore nonsense DOT.');
		}
		break;
	    case 'BACKSPACE':
		if (this._calcDone || this._partialCalcDone) {
		    ecalc.vm.log('Warning: ignore nonsense BACKSPACE.');
		} else {
		    if (this._partialNumber.last() === '.') {
			this._acceptDot = true;
			if (this._partialNumber === '0.') {
			    this._partialNumber = '';
			}
		    }
		    this._partialNumber = this._partialNumber.slice(0, -1);
		}
		break;
	    case 'EQUAL':
		if (! this._calcDone) {
		    this._acceptPartialNumber();
		    this._doCalculationMaybe();
		    this._calcDone = true;
		    this.history.calcDone();
		} else {
		    ecalc.vm.log('Warning: ignore nonsense EQUAL.');
		}
		break;
	    case 'ADD':
	    case 'SUBTRACT':
		if (this._calcDone) {
		    this._calcDone = false;
		} else {
		    this._acceptPartialNumber();
		}
		this._doCalculationMaybe();

		this._opStack.push(key);
		this.history.pushOp(key);

		break;
	    default:
		// never reacher
		utils.assert(false,
			    'function for Key [' + key + '] Not implemented.');
	    }
	}
    }
};
