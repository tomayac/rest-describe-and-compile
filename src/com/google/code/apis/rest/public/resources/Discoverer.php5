<?php
	/** 
	 * This file EXECUTES code that has been generated by REST Compile. 
	 * 
	 * You should not modify it, unless you know what you do. Any modification 
	 * might cause serious damage, or even destroy your computer. 
	 * 
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
	 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
	 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
	 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
	 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
	 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
	 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
	 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
	 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
	 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
	 * POSSIBILITY OF SUCH DAMAGE. 
	 */ 

	// make sure that only local callers execute the script
	if (($_SERVER['REMOTE_ADDR'] != '127.0.0.1') &&
	    ($_SERVER['REQUEST_METHOD'] != 'POST') &&
	    (substr($_SERVER['QUERY_STRING'], 0, 5) != 'data=')) {
		return;	
	}
	
	// create a temporary file and write the received dog food code in it
	$tmpfname = tempnam("/tmp", "Discoverer");	
	$handle = fopen($tmpfname, "w");
	fwrite($handle, stripslashes($_POST['data']));
	
	// this is the part where the received script gets EXECUTED 
	require_once($tmpfname);
	
	// close and unlink the temporary file
	fclose($handle);
	unlink($tmpfname);
?>